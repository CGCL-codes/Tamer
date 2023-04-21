package praktikumid.k10.p14;

public class GoogleGeoCoder extends GeoCoder {

    @Override
    public Coordinates getCoordinates(String location) {
        String url = this.getUrl(location);
        String response = this.getResponse(url);
        Coordinates c = this.parseResponse(response);
        return c;
    }

    private String getUrl(String location) {
        return "http://www.google.com/.../" + location;
    }

    private String getResponse(String url) {
        return "12,12,12,12";
    }

    private Coordinates parseResponse(String response) {
        Coordinates c = new Coordinates();
        c.setLatitude("12");
        return c;
    }
}
