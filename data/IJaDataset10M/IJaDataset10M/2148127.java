package db;

import java.util.HashSet;
import java.util.Set;

public class Account {

    private Long id;

    private String accountName;

    private String password;

    private String displayName;

    private Set<Channel> channels = new HashSet<Channel>();

    protected Account() {
    }

    public Account(String accountName) {
        this.accountName = accountName;
    }

    protected Set<Channel> getChannels() {
        return channels;
    }

    protected void setChannels(Set<Channel> value) {
        channels = value;
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
        channel.getMembers().add(this);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
        channel.getMembers().remove(this);
    }

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return String.format("[%d]%s (%s)", getId(), getAccountName(), getDisplayName());
    }

    public void __printChannels() {
        for (Channel c : channels) {
            System.out.println(c);
        }
    }
}
