package org.fultest.test.qa.tester;

import org.fultest.test.qa.QAClient;
import org.fultest.test.qa.QARequest;
import org.fultest.test.qa.QAResponse;
import org.fultest.test.qa.QAResponseValidator;
import org.fultest.test.qa.QATester;
import org.fultest.test.qa.RequestLoader;
import org.fultest.test.qa.ResponseValidatorLoader;
import org.fultest.test.qa.loader.HttpStatusValidatorLoader;
import org.fultest.test.qa.loader.MapRequestLoader;
import org.restlet.data.Method;

/**
 * A FormTester sends data to an http service as form information.  This is pretty standard for http, however
 * some http methods may not be too happy with it (e.g. PUT).
 *
 * @version $Id: $
 */
public abstract class FormTester implements QATester {

    public QAResponse send(Method operation, String URI, String... requestFiles) {
        return send(new MapRequestLoader(createClient(operation)), URI, requestFiles);
    }

    public QAResponse send(Method operation, String URI) {
        return send(operation, URI, "");
    }

    public QAResponse sendRaw(Method operation, String URI, String content) {
        RequestLoader loader = new MapRequestLoader(createClient(operation));
        QARequest request = loader.createRequest(content);
        return request.send(URI);
    }

    public QAResponse validate(QAResponse response, String responseFile, int status) {
        return validate(createValidatorLoader(status), response);
    }

    public QAResponse validate(QAResponse response, String responseFile) {
        return validate(response, responseFile, 200);
    }

    public QAResponse validateExpectedText(QAResponse response, String expectedText) {
        return validate(response, expectedText, 200);
    }

    protected QAResponse validate(ResponseValidatorLoader resValidatorLoader, QAResponse response) {
        QAResponseValidator qaResponseValidator = resValidatorLoader.load();
        qaResponseValidator.validate(response);
        return response;
    }

    protected QAResponse send(RequestLoader loader, String URI, String... requestFiles) {
        QARequest request = loader.load(requestFiles);
        return request.send(URI);
    }

    protected ResponseValidatorLoader createValidatorLoader(int status) {
        return new HttpStatusValidatorLoader(status);
    }

    protected abstract QAClient createClient(Method operation);
}
