package net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import ui.CaptchaScroller;
import ui.CaptchaScroller.AnswerListener;

/**
 * Handle all of the posting operations.
 * We take a board or thread, create or append to the thread as necessary, and allow cancellation.
 * Various lifecycle events are delivered via Swing thread to update the UI.
 */
public class Poster implements Runnable, AnswerListener {

    /**
	 * The posting operation will have one of these kinds of responses.
	 */
    public enum Result {

        /** Posted okay! */
        Success, /** Throttle back. */
        Flooded, /** Try a different captcha (right away.) */
        BadCaptcha, /** File too big, etc. */
        Rejected, /** Thread disappeared, protocol/network errors, etc. all halt us. */
        TotalFailure
    }

    private int postCounter = 1;

    private final int finalPost;

    private final Thread thread;

    private final List<File> files;

    /**
	 * These lifecycle events occur on the Swing thread.
	 */
    public interface Lifecycle {

        public void done();

        /**
		 * @param latestResponse is null if this is a requested pause
		 */
        public void halted(String latestResponse);

        public void filePostAttempt(File f);

        public void filePostAttempted(File f, Result result);

        public void filePostDone(File f, Result result);

        public void enteredMessageThread(String threadNumber, boolean createdAnew);
    }

    public void stop() {
        thread.interrupt();
    }

    private boolean paused = false;

    public synchronized void pause() {
        paused = true;
        notify();
    }

    private synchronized boolean isPaused() {
        return paused;
    }

    public synchronized void resume() {
        notify();
    }

    private final String name, email, password;

    private final String initialSubject, initialBody, subsequentSubject, subsequentBody;

    private final CaptchaScroller captchas;

    public Poster(Lifecycle callbacks, CaptchaScroller captchas, List<File> files, URL address, String httpUserAgent, String name, String email, String password, String initialSubject, String initialBody, String subsequentSubject, String subsequentBody, Double postDelay) {
        if (!httpUserAgent.equals("")) httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, httpUserAgent);
        if (postDelay != null) setPostDelay(postDelay);
        finalPost = files.size();
        lifecycle = callbacks;
        this.captchas = captchas;
        this.files = files;
        this.address = address;
        this.name = name;
        this.email = email;
        this.password = password;
        this.initialBody = initialBody;
        this.initialSubject = initialSubject;
        this.subsequentBody = subsequentBody;
        this.subsequentSubject = subsequentSubject;
        captchas.setAnswerListener(this);
        captchas.setBoardURL(address);
        captchas.getChallenges(files.size());
        thread = new Thread(this);
        thread.start();
    }

    private final Lifecycle lifecycle;

    private int MAX_FILE_SIZE;

    private String action;

    @Override
    public void run() {
        boolean first = true;
        try {
            boardOrThread = PageCache.fetch(address);
            action = getPostAction();
            MAX_FILE_SIZE = Integer.valueOf(getHiddenValue("MAX_FILE_SIZE"));
            while (!files.isEmpty()) {
                File f = files.get(0);
                final Result result;
                if ((result = postFile(f, first, files.size() == 1)) == Result.TotalFailure || isPaused()) {
                    synchronized (this) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                lifecycle.halted(result == Result.TotalFailure ? latestResponse : null);
                            }
                        });
                        wait();
                        paused = false;
                    }
                }
                if (result == Result.Success || result == Result.Rejected) {
                    first = false;
                    postCounter++;
                    files.remove(0);
                }
            }
        } catch (InterruptedException done) {
        } catch (final Exception e) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null, "Unable to load board: " + e, "Post initialization failure", JOptionPane.ERROR_MESSAGE);
                }
            });
        } finally {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    lifecycle.done();
                }
            });
        }
    }

    private static class Captcha {

        String challenge, response;

        Captcha(String challenge, String response) {
            this.challenge = challenge;
            this.response = response;
        }
    }

    private List<Captcha> captchaAnswers = new LinkedList<Captcha>();

    @Override
    public void answer(String challengeKey, String challengeResponse) {
        synchronized (captchaAnswers) {
            captchaAnswers.add(new Captcha(challengeKey, challengeResponse));
            captchaAnswers.notify();
        }
    }

    private Captcha getCaptcha() throws InterruptedException {
        synchronized (captchaAnswers) {
            if (captchaAnswers.isEmpty()) captchaAnswers.wait();
            return captchaAnswers.remove(0);
        }
    }

    private final HttpClient httpclient = new DefaultHttpClient();

    private String latestResponse = null;

    private int extraChallenges = 0;

    private Result latestResult;

    /**
	 * Try to post a file and perform a post delay in between each attempt.
	 * @param f
	 * @param isFirst
	 * @param isLast
	 * @throws InterruptedException
	 * @throws UnsupportedEncodingException 
	 * @return possible indicator of fatal error which should halt everything
	 */
    private Result postFile(final File f, boolean isFirst, boolean isLast) throws InterruptedException, UnsupportedEncodingException {
        for (; ; ) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    lifecycle.filePostAttempt(f);
                }
            });
            boolean doNotTry = false;
            if (!f.isFile() || !f.canRead()) {
                doNotTry = true;
                latestResponse = "File " + f + " is nonexistant, unreadable, or not a regular file.";
            } else if (f.length() > MAX_FILE_SIZE) {
                doNotTry = true;
                latestResponse = "File " + f + " is larger than " + MAX_FILE_SIZE + " bytes.";
            }
            if (doNotTry) {
                latestResult = Result.Rejected;
                extraChallenges++;
                break;
            }
            Captcha captcha = getCaptcha();
            MultipartEntity formPost = generateFormPost(f, captcha, isFirst, isLast);
            HttpPost post = new HttpPost(action);
            post.setEntity(formPost);
            try {
                HttpResponse r = httpclient.execute(post);
                InputStream is = r.getEntity().getContent();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                int ch;
                StringBuilder s = new StringBuilder();
                while ((ch = isr.read()) != -1) s.append((char) ch);
                latestResponse = s.toString();
                if (r.getStatusLine().getStatusCode() == 200) latestResult = interpretResponse(); else latestResult = Result.TotalFailure;
            } catch (ClientProtocolException e) {
                latestResponse = e.toString();
                latestResult = Result.TotalFailure;
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) throw new InterruptedException();
                latestResponse = e.toString();
                latestResult = Result.TotalFailure;
            }
            if (latestResult == Result.Success || latestResult == Result.TotalFailure || latestResult == Result.Rejected) break;
            if (extraChallenges == 0) captchas.getMoreChallenges(1); else extraChallenges--;
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    lifecycle.filePostAttempted(f, latestResult);
                }
            });
            if (latestResult == Result.Flooded) {
                synchronized (this) {
                    if (!paused) wait(postDelay);
                    if (paused) return latestResult;
                }
            }
        }
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                lifecycle.filePostDone(f, latestResult);
            }
        });
        if (latestResult == Result.Success && !isLast) {
            synchronized (this) {
                if (!paused) wait(postDelay);
            }
        }
        return latestResult;
    }

    private static final Pattern successRegex = Pattern.compile("<!-- thread:[0-9]+,no:([0-9]+) -->"), badCaptchaRegex = Pattern.compile("You (seem to have mistyped|forgot to type in) the verification\\."), floodRegex = Pattern.compile("flood detected", Pattern.CASE_INSENSITIVE), badFileRegex = Pattern.compile("duplicate file entry|upload failed|resolution is too large|file too large|cannot find record|contains embedded archive|possible malicious code", Pattern.CASE_INSENSITIVE);

    /**
	 * Figure out whether we posted successfully, started a new thread, etc.
	 */
    private Result interpretResponse() {
        Matcher success = successRegex.matcher(latestResponse);
        if (success.find()) {
            if (threadNumber == null) {
                threadNumber = success.group(1);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        lifecycle.enteredMessageThread(threadNumber, true);
                    }
                });
            }
            return Result.Success;
        }
        if (badCaptchaRegex.matcher(latestResponse).find()) return Result.BadCaptcha;
        if (floodRegex.matcher(latestResponse).find()) return Result.Flooded;
        if (badFileRegex.matcher(latestResponse).find()) return Result.Rejected;
        return Result.TotalFailure;
    }

    public synchronized void setPostDelay(double seconds) {
        postDelay = (long) (seconds * 1000.0);
    }

    public static double DEFAULT_POST_DELAY = 60.0;

    private long postDelay = (long) (DEFAULT_POST_DELAY * 1000l);

    private String threadNumber = null;

    /**
	 * Generate the form posting, substituting in &currentpost;, &finalpost; and &captcha;.
	 * @param f
	 * @param isFirst
	 * @param isLast
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
    private MultipartEntity generateFormPost(File f, Captcha c, boolean isFirst, boolean isLast) throws UnsupportedEncodingException {
        final Charset utf8 = Charset.forName("UTF-8");
        MultipartEntity e = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, null);
        String subject, body;
        if (isFirst) {
            subject = initialSubject;
            body = initialBody;
        } else {
            subject = subsequentSubject;
            body = subsequentBody;
        }
        subject = doReplacements(subject, c);
        body = doReplacements(body, c);
        if (isLast) body += "\r\n\r\nThis contribution brought to you courtesy of the CaptchasMoot Image Floodulator.";
        e.addPart("MAX_FILE_SIZE", new StringBody(Integer.toString(MAX_FILE_SIZE), utf8));
        if (threadNumber == null) {
            threadNumber = getHiddenValue("resto");
            if (threadNumber != null) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        lifecycle.enteredMessageThread(threadNumber, false);
                    }
                });
            }
        }
        if (threadNumber != null) e.addPart("resto", new StringBody(threadNumber, utf8));
        e.addPart("name", new StringBody(name, utf8));
        e.addPart("email", new StringBody(email, utf8));
        e.addPart("sub", new StringBody(subject, utf8));
        e.addPart("com", new StringBody(body, utf8));
        e.addPart("recaptcha_challenge_field", new StringBody(c.challenge, utf8));
        e.addPart("recaptcha_response_field", new StringBody(c.response, utf8));
        String[] fileSplit = f.getName().split("\\.");
        String fileExt = fileSplit[fileSplit.length - 1].toLowerCase();
        String imageMimeType;
        if (fileExt.startsWith("jp")) imageMimeType = "image/jpeg"; else imageMimeType = "image/" + fileExt;
        e.addPart("upfile", new FileBody(f, imageMimeType));
        e.addPart("pwd", new StringBody(password, utf8));
        e.addPart("mode", new StringBody("regist", utf8));
        return e;
    }

    private String doReplacements(String s, Captcha c) {
        return s.replace("&currentpost;", String.valueOf(postCounter)).replace("&finalpost;", String.valueOf(finalPost)).replace("&captcha;", c.response);
    }

    String boardOrThread;

    URL address;

    /**
	 * Retrieve a hidden value <input> from the form.
	 * @param name
	 * @return the value or null if not found
	 */
    private String getHiddenValue(String name) {
        Pattern regex = Pattern.compile("<input type=\"?hidden\"? name=\"?" + name + "\"? value=\"?([0-9]+)\"?>");
        Matcher m = regex.matcher(boardOrThread);
        if (m.find()) return m.group(1); else return null;
    }

    private String getPostAction() {
        Pattern regex = Pattern.compile("<form name=\"post\" action=\"([^\"]*)\" method=\"POST\" enctype=\"multipart/form-data\">");
        Matcher m = regex.matcher(boardOrThread);
        if (m.find()) return m.group(1); else return null;
    }
}
