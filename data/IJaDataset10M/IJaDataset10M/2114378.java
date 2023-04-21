package bg.bulsi.municipalityweb.esign;

import java.io.IOException;
import java.security.cert.X509Certificate;
import netscape.javascript.JSObject;
import bg.bulsi.municipalityweb.esign.sign.util.DigitalSignatureException;
import bg.bulsi.municipalityweb.esign.sign.util.applet.SmartCardApplet;
import bg.bulsi.municipalityweb.esign.sign.util.xml.CertificateSignUtils;
import bg.bulsi.municipalityweb.esign.sign.util.xml.CertificationChainAndSignatureBase64;

public class ECertificateLoginApplet extends SmartCardApplet {

    private static final long serialVersionUID = 1L;

    private static final String FORM_ID = "formId";

    private static final String TOKEN_PARAMETER = "loginToken";

    private static final String LOGIN_MESSAGE = "loginMessage";

    private static final String LOGIN_BUTTON = "loginButton";

    protected void signButtonPressed(X509Certificate certificate, JSObject browserWindow, CertificationChainAndSignatureBase64 certificationChain) throws DigitalSignatureException {
        String formId = getParameter(FORM_ID);
        String tokenId = getParameter(TOKEN_PARAMETER);
        String loginMessageId = getParameter(LOGIN_MESSAGE);
        String loginButtonId = getParameter(LOGIN_BUTTON);
        JSObject mainForm = (JSObject) browserWindow.eval(new StringBuilder().append("document.getElementById('").append(formId).append("')").toString());
        System.out.println("find token");
        JSObject tokenField = (JSObject) mainForm.getMember(tokenId);
        String token = (String) tokenField.getMember("value");
        System.out.println("create login message");
        StringBuilder message = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        message.append("<login><token>");
        message.append(token);
        message.append("</token></login>");
        try {
            System.out.println("sign login message");
            String signedMessage = CertificateSignUtils.signXML(message.toString(), certificationChain.privateKeyAndCertChain);
            System.out.println("submit the form");
            JSObject messageField = (JSObject) mainForm.getMember(loginMessageId);
            messageField.setMember("value", signedMessage);
            mainForm.eval(new StringBuilder().append("document.getElementById('").append(loginButtonId).append("').onclick();").toString());
            return;
        } catch (IOException e) {
            throw new DigitalSignatureException(e.getMessage(), e);
        }
    }
}
