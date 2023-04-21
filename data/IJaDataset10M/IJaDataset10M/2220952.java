package whf.framework.workflow.assign.parser;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.jdom.Element;
import whf.framework.exception.AppException;
import whf.framework.exception.FindException;
import whf.framework.workflow.assign.AssignmentService;

/**
 * @author king
 *
 */
public class RoleAssignmentParser extends DefaultAssignmentParser implements RoleableAssignmentParser, DeptableAssignmentParser {

    private Collection<String> deptCodes;

    private String roleName;

    @Override
    protected void parseSourceElement(Element source) throws AppException {
        this.setRoleName(super.getAttribute("name"));
    }

    public Set<AssignmentParser> getChildrenParsers() {
        Set<AssignmentParser> children = super.getChildrenParsers();
        for (Iterator<AssignmentParser> it = children.iterator(); it.hasNext(); ) {
            AssignmentParser p = it.next();
            if (!(p instanceof UserAssignmentParser)) {
                it.remove();
            }
        }
        return children;
    }

    public Collection<String> getUsers() throws FindException {
        Set<AssignmentParser> children = this.getChildrenParsers();
        if (children == null || children.isEmpty()) {
            return AssignmentService.getAssignmentService().findUsersByRole(this.deptCodes, this.roleName);
        } else {
            return null;
        }
    }

    public Collection<String> getDeptCodes() {
        return this.deptCodes;
    }

    public void setDeptCodes(Collection<String> deptCodes) {
        this.deptCodes = deptCodes;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
