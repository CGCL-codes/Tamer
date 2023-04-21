package org.restlet.client.engine.http.header;

import static org.restlet.client.engine.http.header.HeaderUtils.isSpace;
import java.io.IOException;
import org.restlet.client.data.ChallengeRequest;
import org.restlet.client.data.ChallengeScheme;
import org.restlet.client.data.Parameter;

/**
 * Challenge request header reader.
 * 
 * @author Thierry Boileau
 */
public class ChallengeRequestReader extends HeaderReader<ChallengeRequest> {

    public static void main(String[] args) throws Exception {
        String str = "Basic realm=\"Control Panel\"";
        ChallengeRequestReader r = new ChallengeRequestReader(str);
        ChallengeRequest c = r.readValue();
        System.out.println(c.getScheme());
        System.out.println(c.getRawValue());
        str = "Digest realm=\"Control Panel\", domain=\"/controlPanel\", nonce=\"15bb54af506016d4414a025d4c84e34c\", algorithm=MD5, qop=\"auth,auth-int\"";
        r = new ChallengeRequestReader(str);
        c = r.readValue();
        System.out.println(c.getScheme());
        System.out.println(c.getRawValue());
        str = "Negotiate";
        r = new ChallengeRequestReader(str);
        c = r.readValue();
        System.out.println(c.getScheme());
        System.out.println(c.getRawValue());
        str = "Basic realm=\"Control Panel\",Digest realm=\"Control Panel\", domain=\"/controlPanel\", nonce=\"15bb54af506016d4414a025d4c84e34c\", algorithm=MD5, qop=\"auth,auth-int\"";
        r = new ChallengeRequestReader(str);
        System.out.println("list");
        for (ChallengeRequest challengeRequest : r.readValues()) {
            System.out.println(challengeRequest.getScheme());
            System.out.println(challengeRequest.getRawValue());
        }
    }

    /**
     * Constructor.
     * 
     * @param header
     *            The header to read.
     */
    public ChallengeRequestReader(String header) {
        super(header);
    }

    @Override
    public ChallengeRequest readValue() throws IOException {
        ChallengeRequest result = null;
        skipSpaces();
        if (peek() != -1) {
            String scheme = readToken();
            result = new ChallengeRequest(new ChallengeScheme("HTTP_" + scheme, scheme));
            skipSpaces();
            HeaderWriter<Parameter> w = new HeaderWriter<Parameter>() {

                @Override
                public HeaderWriter<Parameter> append(Parameter value) {
                    appendExtension(value);
                    return this;
                }
            };
            boolean stop = false;
            while (peek() != -1 && !stop) {
                boolean sepSkipped = skipValueSeparator();
                mark();
                readToken();
                int nextChar = read();
                reset();
                if (isSpace(nextChar)) {
                    stop = true;
                } else {
                    if (sepSkipped) {
                        w.appendValueSeparator();
                    }
                    w.append(readParameter());
                }
            }
            result.setRawValue(w.toString());
        }
        return result;
    }
}
