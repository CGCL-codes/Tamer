package simple;

import simple.http.load.RedirectService;
import simple.template.Environment;
import simple.http.serve.Resource;
import simple.http.serve.Context;
import simple.http.Request;
import simple.http.Response;
import simple.track.Track;
import java.net.InetAddress;

public class TrackAction extends RedirectService {

    private Track tracker;

    public TrackAction(Context context) {
        super(context);
    }

    public void prepare(Environment env) {
        tracker = new Track(context);
    }

    public Resource redirect(Request req, Response resp) throws Exception {
        String source = req.getValue("Referer");
        String agent = req.getValue("User-Agent");
        InetAddress client = req.getInetAddress();
        tracker.track(client, source, agent);
        return lookup("home");
    }
}
