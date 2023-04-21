package net.sf.osadm.mpso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * This visitor creates a Comma Separated Values file, one row for each Maven project found.
 */
public class CsvMpsoVisitor implements MavenProjectInformationVisitor {

    private StringBuilder builder = new StringBuilder();

    private String NL = System.getProperty("line.separator");

    private DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

    @Override
    public void runThrough(MavenProjectInformationVisitorAcceptor acceptor) {
        beforeVisit();
        acceptor.accept(this);
        afterVisit();
    }

    public void beforeVisit() {
        List<String> titleList = Arrays.asList(new String[] { "group id", "artifact id", "version", "type", "name", "last published", "url" });
        boolean isFirst = true;
        for (String title : titleList) {
            if (!isFirst) {
                builder.append(", ");
            }
            builder.append(title);
            isFirst = false;
        }
    }

    @Override
    public void visit(MavenProjectInformation mpi) {
        if (builder.length() > 0) {
            builder.append(NL);
        }
        builder.append(mpi.getGroupId());
        builder.append(", ");
        builder.append(mpi.getArtifactId());
        builder.append(", ");
        builder.append(mpi.getVersion());
        builder.append(", ");
        builder.append(mpi.getType());
        builder.append(", ");
        builder.append(mpi.getName());
        builder.append(", ");
        builder.append((mpi.getLastPublished() != null) ? dateFormat.format(mpi.getLastPublished()) : "null");
        builder.append(", ");
        builder.append(mpi.getHomePageUrl());
    }

    public void afterVisit() {
    }

    public String getAsText() {
        return builder.toString();
    }
}
