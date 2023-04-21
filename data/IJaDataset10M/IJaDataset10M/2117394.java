package datadog.auction.model;

public class UserId {

    private String username;

    private String organizationId;

    public UserId(String username, String organizationId) {
        this.username = username;
        this.organizationId = organizationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId)) return false;
        final UserId userId = (UserId) o;
        if (!organizationId.equals(userId.organizationId)) return false;
        if (!username.equals(userId.username)) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = username.hashCode();
        result = 29 * result + organizationId.hashCode();
        return result;
    }
}
