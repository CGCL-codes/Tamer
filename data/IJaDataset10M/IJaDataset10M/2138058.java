package database;

import java.util.Date;
import java.util.List;

/**
 *
 * @author f
 */
public class Filter {

    private String description;

    private int id;

    private String name;

    private String password;

    private String url;

    private Date dateFrom;

    private Date dateTo;

    private List<String> facilities;

    private List<String> severities;

    private List<String> servers;

    private List<String> tags;

    private String message;

    public Filter() {
    }

    public Filter(String description, int id, String name, String password, String url, Date dateFrom, Date dateTo, List<String> facilities, List<String> severities, List<String> servers, List<String> tags, String message) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.password = password;
        this.url = url;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.facilities = facilities;
        this.severities = severities;
        this.servers = servers;
        this.tags = tags;
        this.message = message;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void ListDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void ListDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getDescription() {
        return description;
    }

    public void ListDescription(String description) {
        this.description = description;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void ListFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    public int getId() {
        return id;
    }

    public void ListId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void ListMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void ListName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void ListPassword(String password) {
        this.password = password;
    }

    public List<String> getServers() {
        return servers;
    }

    public void ListServers(List<String> servers) {
        this.servers = servers;
    }

    public List<String> getSeverities() {
        return severities;
    }

    public void ListSeverities(List<String> severities) {
        this.severities = severities;
    }

    public List<String> getTags() {
        return tags;
    }

    public void ListTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUrl() {
        return url;
    }

    public void ListUrl(String url) {
        this.url = url;
    }
}
