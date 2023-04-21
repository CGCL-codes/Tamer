package issrg.ontology.exporter.permis;

import org.w3c.dom.*;
import issrg.ontology.*;

public class TargetAccessBuilder extends PERMISPolicyBuilder {

    public TargetAccessBuilder(PolicyOntology ontology, Document doc, Element parent) {
        super(ontology, doc, parent);
    }

    public void build() {
        try {
            Element taPolicy = doc.createElement(PERMISPolicyBuilder.TARGET_ACCESS_POLICY);
            parent.appendChild(taPolicy);
            OntologyClass roleClass = ontology.getOntologyClass(PolicyOntology.ROLE_CLASS);
            RoleInstance allRoles = (RoleInstance) roleClass.getGenericInstance();
            if (allRoles != null && allRoles.getPermissions() != null) {
                for (PermissionInstance permission : allRoles.getPermissions()) {
                    ActionInstance action = permission.getAction();
                    ResourceInstance resource = permission.getResource();
                    Element targetAccess = buildTargetAccess(allRoles.getName() + "_" + action.getName() + "_" + resource.getName());
                    Element targetList = (Element) targetAccess.getElementsByTagName("TargetList").item(0);
                    buildTargetList(targetList, action, resource);
                }
            }
            for (OntologyInstance inst : roleClass.getAllInstances()) {
                RoleInstance role = (RoleInstance) inst;
                if (role != null && role.getPermissions() != null) {
                    for (PermissionInstance permission : role.getPermissions()) {
                        ActionInstance action = permission.getAction();
                        ResourceInstance resource = permission.getResource();
                        Element targetAccess = buildTargetAccess(role.getName() + "_" + action.getName() + "_" + resource.getName());
                        Element roleList = (Element) targetAccess.getElementsByTagName("RoleList").item(0);
                        Element roleElement = doc.createElement("Role");
                        roleList.appendChild(roleElement);
                        String name = role.getName();
                        String type = RoleHierarchyBuilder.DEAFULT_ROLE_TYPE;
                        roleElement.setAttribute("Type", type);
                        roleElement.setAttribute("Value", name);
                        Element roleTargetList = (Element) targetAccess.getElementsByTagName("TargetList").item(0);
                        buildTargetList(roleTargetList, action, resource);
                    }
                }
            }
            for (OntologyClass subRoleClass : roleClass.getSubClasses()) {
                RoleInstance allSubRoles = (RoleInstance) subRoleClass.getGenericInstance();
                if (allSubRoles != null && allSubRoles.getPermissions() != null) {
                    for (PermissionInstance permission : allSubRoles.getPermissions()) {
                        ActionInstance action = permission.getAction();
                        ResourceInstance resource = permission.getResource();
                        Element targetAccess = buildTargetAccess(allSubRoles.getName() + "_" + action.getName() + "_" + resource.getName());
                        Element roleList = (Element) targetAccess.getElementsByTagName("RoleList").item(0);
                        for (OntologyInstance inst : subRoleClass.getAllInstances()) {
                            RoleInstance role = (RoleInstance) inst;
                            Element roleElement = doc.createElement("Role");
                            roleList.appendChild(roleElement);
                            String name = role.getName();
                            String type = role.getType();
                            roleElement.setAttribute("Type", type);
                            roleElement.setAttribute("Value", name);
                        }
                        Element targetList = (Element) targetAccess.getElementsByTagName("TargetList").item(0);
                        buildTargetList(targetList, action, resource);
                    }
                    for (OntologyInstance inst : subRoleClass.getAllInstances()) {
                        RoleInstance role = (RoleInstance) inst;
                        if (role != null && role.getPermissions() != null) {
                            for (PermissionInstance permission : role.getPermissions()) {
                                ActionInstance action = permission.getAction();
                                ResourceInstance resource = permission.getResource();
                                Element subRoleTargetAccess = buildTargetAccess(role.getName() + "_" + action.getName() + "_" + resource.getName());
                                Element subRoleList = (Element) subRoleTargetAccess.getElementsByTagName("RoleList").item(0);
                                Element subRoleElement = doc.createElement("Role");
                                subRoleList.appendChild(subRoleElement);
                                String name = role.getName();
                                String type = role.getType();
                                subRoleElement.setAttribute("Type", type);
                                subRoleElement.setAttribute("Value", name);
                                Element subRoleTargetList = (Element) subRoleTargetAccess.getElementsByTagName("TargetList").item(0);
                                buildTargetList(subRoleTargetList, action, resource);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private Element buildTargetAccess(String id) {
        Element targetAccess = doc.createElement("TargetAccess");
        parent.getElementsByTagName(PERMISPolicyBuilder.TARGET_ACCESS_POLICY).item(0).appendChild(targetAccess);
        targetAccess.setAttribute("ID", id);
        Element roleList = doc.createElement("RoleList");
        Element targetList = doc.createElement("TargetList");
        targetAccess.appendChild(roleList);
        targetAccess.appendChild(targetList);
        return targetAccess;
    }

    private void buildTargetList(Element targetList, ActionInstance action, ResourceInstance resource) {
        if (resource.getName().startsWith("All_")) {
            OntologyClass resourceClass = ontology.getOntologyClass(resource.getType());
            for (OntologyInstance inst : resourceClass.getAllInstances()) {
                ResourceInstance eachRes = (ResourceInstance) inst;
                buildTargetList(targetList, action, eachRes);
            }
        } else {
            Element target = doc.createElement("Target");
            targetList.appendChild(target);
            target.setAttribute("Actions", action.getName());
            Element targetDomain = doc.createElement("TargetDomain");
            target.appendChild(targetDomain);
            targetDomain.setAttribute("ID", resource.getName());
        }
    }
}
