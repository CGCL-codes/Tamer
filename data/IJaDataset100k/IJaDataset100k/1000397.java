package restful;

public class PathProtocol extends RestfulProtocol {

    public PathProtocol() {
        topic("Path");
        resource("Path");
        {
            GET("/path/{path:.*}/documents");
            GET("/path/{path:.*}");
            GET("/path");
        }
    }
}
