package net.sf.osadm.mpso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MavenProjectInformationBuilder {

    private String groupId;

    private String artifactId;

    private String version;

    private String type;

    private String name;

    private String description;

    private URL homePageUrl;

    private Date lastPublished;

    private String organizationName;

    private URL organizationUrl;

    private URI baseProjectSiteUri;

    private URI projectSiteUri;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.hh");

    public MavenProjectInformation create() {
        MavenProjectInformation mavenProjectInformation = new MavenProjectInformation(groupId, artifactId, version, type, name, description, homePageUrl, organizationName, organizationUrl, lastPublished, baseProjectSiteUri, projectSiteUri);
        reset();
        return mavenProjectInformation;
    }

    private void reset() {
        groupId = null;
        artifactId = null;
        version = null;
        type = null;
        name = null;
        description = null;
        homePageUrl = null;
        lastPublished = null;
        organizationName = null;
        organizationUrl = null;
        baseProjectSiteUri = null;
        projectSiteUri = null;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHomePageUrl(URL homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    public void setLastPublished(Date lastPublished) {
        this.lastPublished = lastPublished;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setOrganizationUrl(URL organizationUrl) {
        this.organizationUrl = organizationUrl;
    }

    public static void setDateFormat(DateFormat dateFormat) {
        MavenProjectInformationBuilder.dateFormat = dateFormat;
    }

    public void setBaseProjectSiteUri(URI baseProjectSiteUri) {
        this.baseProjectSiteUri = baseProjectSiteUri;
    }

    public void setProjectSiteUri(URI projectSiteUri) {
        this.projectSiteUri = projectSiteUri;
    }

    public void set(String fieldName, String fieldValue) {
        if ("groupId".equalsIgnoreCase(fieldName)) {
            setGroupId(fieldValue);
        } else if ("artifactId".equalsIgnoreCase(fieldName)) {
            setArtifactId(fieldValue);
        } else if ("version".equalsIgnoreCase(fieldName)) {
            setVersion(fieldValue);
        } else if ("type".equalsIgnoreCase(fieldName)) {
            setType(fieldValue);
        } else if ("name".equalsIgnoreCase(fieldName)) {
            setName(fieldValue);
        } else if ("description".equalsIgnoreCase(fieldName)) {
            setDescription(fieldValue);
        } else if ("homePageUrl".equalsIgnoreCase(fieldName)) {
            try {
                setHomePageUrl(new URL(fieldValue));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.err.println("Unable to parse incomming home page url '" + fieldValue + "' into URL instance.");
            }
        } else if ("organizationName".equalsIgnoreCase(fieldName)) {
            setOrganizationName(fieldValue);
        } else if ("organizationUrl".equalsIgnoreCase(fieldName)) {
            try {
                setOrganizationUrl(new URL(fieldValue));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.err.println("Unable to parse incomming organization url '" + fieldValue + "' into URL instance.");
            }
        } else if ("lastPublished".equalsIgnoreCase(fieldName)) {
            try {
                setLastPublished(dateFormat.parse(fieldValue));
            } catch (ParseException e) {
                e.printStackTrace();
                System.err.println("Unable to parse incomming last published date '" + fieldValue + "'.");
            }
        }
    }
}
