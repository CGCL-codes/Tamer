package javango.http;

public class HttpException extends Exception {

    public HttpException() {
        super();
    }

    public HttpException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public HttpException(String arg0) {
        super(arg0);
    }

    public HttpException(Throwable arg0) {
        super(arg0);
    }
}
