package presentor;

import java.util.Collection;
import java.util.logging.Logger;
import provider.CsmJob;
import provider.CsmJobParser.Work;

public class CsmRssPresentor {

    private CsmJob job;

    private static final Logger LOG = Logger.getLogger("CsmRssPresentor");

    public CsmRssPresentor(CsmJob job) {
        this.job = job;
    }

    public String getRss() {
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><rss version=\"2.0\"><channel>");
        if (job != null) {
            builder.append("<title>");
            builder.append(job.getName());
            builder.append("</title>");
            Collection<Work> works = job.getWorks();
            LOG.info("Generating RSS for work size: " + works.size());
            for (Work work : works) {
                builder.append("<item>");
                builder.append("<title>");
                builder.append(escape(work.getJob()));
                builder.append("</title>");
                builder.append("<description>");
                builder.append(escape(work.getDetail()));
                builder.append("</description>");
                builder.append("</item>");
            }
        }
        builder.append("</channel></rss>");
        return builder.toString();
    }

    private String escape(String html) {
        return "<![CDATA[" + html + "]]>";
    }
}
