package org.apache.shindig.gadgets.servlet;

import org.apache.commons.io.IOUtils;
import org.apache.shindig.common.servlet.InjectedServlet;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.inject.Inject;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Handles RPC metadata requests.
 */
public class RpcServlet extends InjectedServlet {

    static final String GET_REQUEST_REQ_PARAM = "req";

    static final String GET_REQUEST_CALLBACK_PARAM = "callback";

    static final Pattern GET_REQUEST_CALLBACK_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9_\\.]+");

    private static final int POST_REQUEST_MAX_SIZE = 1024 * 128;

    private static final Logger logger = Logger.getLogger("org.apache.shindig.gadgets");

    private JsonRpcHandler jsonHandler;

    @Inject
    public void setJsonRpcHandler(JsonRpcHandler jsonHandler) {
        this.jsonHandler = jsonHandler;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reqValue;
        String callbackValue;
        try {
            reqValue = validateParameterValue(request, GET_REQUEST_REQ_PARAM);
            callbackValue = validateParameterValue(request, GET_REQUEST_CALLBACK_PARAM);
            if (!GET_REQUEST_CALLBACK_PATTERN.matcher(callbackValue).matches()) {
                throw new IllegalArgumentException("Wrong format for parameter '" + GET_REQUEST_CALLBACK_PARAM + "' specified. Expected: " + GET_REQUEST_CALLBACK_PATTERN.toString());
            }
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.log(Level.INFO, e.getMessage(), e);
            return;
        }
        Result result = process(request, response, reqValue.getBytes());
        response.getWriter().write(result.isSuccess() ? callbackValue + '(' + result.getOutput() + ')' : result.getOutput());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream is = request.getInputStream();
        byte[] body = IOUtils.toByteArray(is);
        Result result = process(request, response, body);
        response.getWriter().write(result.getOutput());
    }

    private String validateParameterValue(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        String result = request.getParameter(parameter);
        if (result == null) {
            throw new IllegalArgumentException("No parameter '" + parameter + "' specified.");
        }
        return result;
    }

    private Result process(HttpServletRequest request, HttpServletResponse response, byte[] body) {
        try {
            String encoding = getRequestCharacterEncoding(request);
            JSONObject req = new JSONObject(new String(body, encoding));
            JSONObject resp = jsonHandler.process(req);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=rpc.txt");
            return new Result(resp.toString(), true);
        } catch (UnsupportedEncodingException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.log(Level.INFO, e.getMessage(), e);
            return new Result("Unsupported input character set", false);
        } catch (JSONException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Result("Malformed JSON request.", false);
        } catch (RpcException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.log(Level.INFO, e.getMessage(), e);
            return new Result(e.getMessage(), false);
        }
    }

    private String getRequestCharacterEncoding(HttpServletRequest request) {
        String encoding = request.getCharacterEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        return encoding;
    }

    private static class Result {

        private final String output;

        private final boolean success;

        public Result(String output, boolean success) {
            this.output = output;
            this.success = success;
        }

        public String getOutput() {
            return output;
        }

        public boolean isSuccess() {
            return success;
        }
    }
}
