package ee.webAppToolkit.core;

public interface WrappingController {

    public void beforeHandling(String memberName, Object controller);

    public Result wrapResult(Result result, String memberName, Object controller);
}
