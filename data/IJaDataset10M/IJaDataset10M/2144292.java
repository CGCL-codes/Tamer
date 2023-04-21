package org.jprovocateur.businesslayer.objectmodel.accessrights;

import org.jprovocateur.businesslayer.objectmodel.user.User;
import org.jprovocateur.support.ApplicationContextSingleton;
import org.jprovocateur.support.ApplicationDefinition;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

/**
 * <p/>
 *
 * @author Michael Pitsounis 
 */
public class UserRights implements java.io.Serializable {

    @NotNull
    @NotEmpty
    private Long id;

    @NotNull
    @NotEmpty
    private User user;

    @NotNull
    @NotEmpty
    private Role role;

    @NotNull
    @NotEmpty
    @Length(max = 1)
    private Long disabled;

    private Long detailRecordPosition;

    private String detailView;

    /** default constructor */
    public UserRights() {
    }

    public Long getDisabled() {
        return this.disabled;
    }

    public void setDisabled(Long disabled) {
        this.disabled = disabled;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getDetailRecordPosition() {
        return detailRecordPosition;
    }

    public void setDetailRecordPosition(Long detailRecordPosition) {
        this.detailRecordPosition = detailRecordPosition;
    }

    public String getDetailView() {
        return detailView;
    }

    public void setDetailView(String detailView) {
        this.detailView = detailView;
    }
}
