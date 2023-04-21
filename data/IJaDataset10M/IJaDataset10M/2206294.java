package org.adapit.wctoolkit.events.actions.proxy;

import java.util.List;
import javax.swing.JOptionPane;
import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.uml.ext.core.Model;
import org.adapit.wctoolkit.uml.ext.core.Package;
import org.adapit.wctoolkit.uml.ext.core.Stereotype;

public class MvcPackagesProxy {

    @SuppressWarnings("unchecked")
    public static org.adapit.wctoolkit.uml.ext.core.Package defineFormPackage(Model root, String childName) {
        if (guiPackage == null) {
            List<org.adapit.wctoolkit.uml.ext.core.Package> list = root.getElements(org.adapit.wctoolkit.uml.ext.core.Package.class);
            for (org.adapit.wctoolkit.uml.ext.core.Package pk : list) {
                if (pk.containsStereotype("FormPackage")) {
                    guiPackage = pk;
                    break;
                }
            }
            if (guiPackage == null) {
                String str = JOptionPane.showInputDialog(DefaultApplicationFrame.getInstance(), "Informe a estrutura de diret�rios do pacote dos formul�rios" + "\n" + "Utilize dois pontos para separar os pacotes.", "com::empresa::sistema::layout");
                if (str == null || str.trim().equals("")) return root;
                org.adapit.wctoolkit.uml.ext.core.Package pk = new org.adapit.wctoolkit.uml.ext.core.Package(root);
                pk.setLastAsText(str);
                pk = (org.adapit.wctoolkit.uml.ext.core.Package) pk.getParentElement().addElement(pk);
                Stereotype st = new Stereotype(pk);
                st.setName("FormPackage");
                pk.assignStereotype(st);
                guiPackage = pk;
            }
        }
        org.adapit.wctoolkit.uml.ext.core.Package pk = new org.adapit.wctoolkit.uml.ext.core.Package(guiPackage);
        pk.setName(childName.toLowerCase());
        pk = (Package) guiPackage.addElement(pk);
        return pk;
    }

    @SuppressWarnings("unchecked")
    public static org.adapit.wctoolkit.uml.ext.core.Package defineFormPackage(Model root) {
        if (guiPackage == null) {
            List<org.adapit.wctoolkit.uml.ext.core.Package> list = root.getElements(org.adapit.wctoolkit.uml.ext.core.Package.class);
            for (org.adapit.wctoolkit.uml.ext.core.Package pk : list) {
                if (pk.containsStereotype("FormPackage")) {
                    guiPackage = pk;
                    break;
                }
            }
            if (guiPackage == null) {
                String str = JOptionPane.showInputDialog(DefaultApplicationFrame.getInstance(), "Informe a estrutura de diret�rios do pacote dos formul�rios" + "\n" + "Utilize dois pontos para separar os pacotes.", "com::empresa::sistema::layout");
                if (str == null || str.trim().equals("")) return root;
                org.adapit.wctoolkit.uml.ext.core.Package pk = new org.adapit.wctoolkit.uml.ext.core.Package(root);
                pk.setLastAsText(str);
                pk = (org.adapit.wctoolkit.uml.ext.core.Package) pk.getParentElement().addElement(pk);
                Stereotype st = new Stereotype(pk);
                st.setName("FormPackage");
                pk.assignStereotype(st);
                guiPackage = pk;
            }
        }
        return guiPackage;
    }

    public static org.adapit.wctoolkit.uml.ext.core.Package defineViewPackage(String packStr) {
        if (guiPackage == null) {
            Model root = DefaultApplicationFrame.getInstance().getSelectedModel();
            if (!isViewPackageAvailable()) {
                String str = packStr.replace(".", "::");
                if (str == null || str.trim().equals("")) return root;
                org.adapit.wctoolkit.uml.ext.core.Package pk = new org.adapit.wctoolkit.uml.ext.core.Package(root);
                pk.setLastAsText(str);
                pk = (org.adapit.wctoolkit.uml.ext.core.Package) pk.getParentElement().addElement(pk);
                Stereotype st = new Stereotype(pk);
                st.setName("FormPackage");
                pk.assignStereotype(st);
                guiPackage = pk;
            }
        }
        return guiPackage;
    }

    private static org.adapit.wctoolkit.uml.ext.core.Package guiPackage = null;

    public static boolean isViewPackageAvailable() {
        if (guiPackage == null) {
            Model root = DefaultApplicationFrame.getInstance().getSelectedModel();
            List<org.adapit.wctoolkit.uml.ext.core.Package> list = root.getElements(org.adapit.wctoolkit.uml.ext.core.Package.class);
            for (org.adapit.wctoolkit.uml.ext.core.Package pk : list) {
                if (pk.containsStereotype("FormPackage")) {
                    guiPackage = pk;
                    break;
                }
            }
        }
        return guiPackage != null;
    }

    public static String getViewPackageNamespace() {
        return guiPackage.getNamespaceWithoutModel().replace("::", ".");
    }

    @SuppressWarnings("unchecked")
    public static org.adapit.wctoolkit.uml.ext.core.Package defineServicePackage(Model root) {
        if (servicePackage == null) {
            List<org.adapit.wctoolkit.uml.ext.core.Package> list = root.getElements(org.adapit.wctoolkit.uml.ext.core.Package.class);
            for (org.adapit.wctoolkit.uml.ext.core.Package pk : list) {
                if (pk.containsStereotype("ServicePackage")) {
                    servicePackage = pk;
                    break;
                }
            }
            if (servicePackage == null) {
                String str = JOptionPane.showInputDialog(DefaultApplicationFrame.getInstance(), "Informe a estrutura de diret�rios do pacote das interface de servi�o" + "\n" + "Utilize dois pontos para separar os pacotes.", "com::empresa::sistema::services");
                if (str == null || str.trim().equals("")) return root;
                org.adapit.wctoolkit.uml.ext.core.Package pk = new org.adapit.wctoolkit.uml.ext.core.Package(root);
                pk.setLastAsText(str);
                pk = (org.adapit.wctoolkit.uml.ext.core.Package) pk.getParentElement().addElement(pk);
                Stereotype st = new Stereotype(pk);
                st.setName("ServicePackage");
                pk.assignStereotype(st);
                servicePackage = pk;
            }
        }
        return servicePackage;
    }

    public static org.adapit.wctoolkit.uml.ext.core.Package defineServicePackage(String packStr) {
        if (servicePackage == null) {
            Model root = DefaultApplicationFrame.getInstance().getSelectedModel();
            if (!isServicePackageAvailable()) {
                String str = packStr.replace(".", "::");
                if (str == null || str.trim().equals("")) return root;
                org.adapit.wctoolkit.uml.ext.core.Package pk = new org.adapit.wctoolkit.uml.ext.core.Package(root);
                pk.setLastAsText(str);
                pk = (org.adapit.wctoolkit.uml.ext.core.Package) pk.getParentElement().addElement(pk);
                Stereotype st = new Stereotype(pk);
                st.setName("ServicePackage");
                pk.assignStereotype(st);
                servicePackage = pk;
            }
        }
        return servicePackage;
    }

    public static boolean isServicePackageAvailable() {
        if (servicePackage == null) {
            Model root = DefaultApplicationFrame.getInstance().getSelectedModel();
            List<org.adapit.wctoolkit.uml.ext.core.Package> list = root.getElements(org.adapit.wctoolkit.uml.ext.core.Package.class);
            for (org.adapit.wctoolkit.uml.ext.core.Package pk : list) {
                if (pk.containsStereotype("ServicePackage")) {
                    servicePackage = pk;
                    break;
                }
            }
        }
        return servicePackage != null;
    }

    public static String getServicePackageNamespace() {
        return servicePackage.getNamespaceWithoutModel().replace("::", ".");
    }

    private static org.adapit.wctoolkit.uml.ext.core.Package servicePackage = null;

    @SuppressWarnings({ "unused", "unchecked" })
    public static org.adapit.wctoolkit.uml.ext.core.Package defineActionPackage(Model root) {
        if (actionPackage == null) {
            List<org.adapit.wctoolkit.uml.ext.core.Package> list = root.getElements(org.adapit.wctoolkit.uml.ext.core.Package.class);
            for (org.adapit.wctoolkit.uml.ext.core.Package pk : list) {
                if (pk.containsStereotype("ActionPackage")) {
                    actionPackage = pk;
                    break;
                }
            }
            if (actionPackage == null) {
                try {
                    String str = JOptionPane.showInputDialog(DefaultApplicationFrame.getInstance(), "Informe a estrutura de diret�rios do pacote das a��es" + "\n" + "Utilize dois pontos para separar os pacotes.", "com::empresa::sistema::actions");
                    if (str == null || str.trim().equals("")) return null;
                    org.adapit.wctoolkit.uml.ext.core.Package pk = new org.adapit.wctoolkit.uml.ext.core.Package(root);
                    pk.setLastAsText(str.trim());
                    pk = (org.adapit.wctoolkit.uml.ext.core.Package) pk.getParentElement().addElement(pk);
                    Stereotype st = new Stereotype(pk);
                    st.setName("ActionPackage");
                    pk.assignStereotype(st);
                    actionPackage = pk;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return actionPackage;
    }

    @SuppressWarnings({ "unused", "unchecked" })
    public static org.adapit.wctoolkit.uml.ext.core.Package defineControllerPackage(String packStr) {
        if (actionPackage == null) {
            Model root = DefaultApplicationFrame.getInstance().getSelectedModel();
            List<org.adapit.wctoolkit.uml.ext.core.Package> list = root.getElements(org.adapit.wctoolkit.uml.ext.core.Package.class);
            if (!isControllerPackageAvailable()) {
                try {
                    String str = packStr.replace(".", "::");
                    if (str == null || str.trim().equals("")) return null;
                    org.adapit.wctoolkit.uml.ext.core.Package pk = new org.adapit.wctoolkit.uml.ext.core.Package(root);
                    pk.setLastAsText(str.trim());
                    pk = (org.adapit.wctoolkit.uml.ext.core.Package) pk.getParentElement().addElement(pk);
                    Stereotype st = new Stereotype(pk);
                    st.setName("ActionPackage");
                    pk.assignStereotype(st);
                    actionPackage = pk;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return actionPackage;
    }

    private static org.adapit.wctoolkit.uml.ext.core.Package actionPackage = null;

    public static boolean isControllerPackageAvailable() {
        if (actionPackage == null) {
            Model root = DefaultApplicationFrame.getInstance().getSelectedModel();
            List<org.adapit.wctoolkit.uml.ext.core.Package> list = root.getElements(org.adapit.wctoolkit.uml.ext.core.Package.class);
            for (org.adapit.wctoolkit.uml.ext.core.Package pk : list) {
                if (pk.containsStereotype("ActionPackage")) {
                    actionPackage = pk;
                    break;
                }
            }
        }
        return actionPackage != null;
    }

    public static String getControllerPackageNamespace() {
        return actionPackage.getNamespaceWithoutModel().replace("::", ".");
    }

    @SuppressWarnings({ "unused", "unchecked" })
    public static org.adapit.wctoolkit.uml.ext.core.Package defineModelPackage(String packStr) {
        if (modelPackage == null) {
            Model root = DefaultApplicationFrame.getInstance().getSelectedModel();
            if (!isModelPackageAvailable()) {
                try {
                    String str = packStr.replace(".", "::");
                    if (str == null || str.trim().equals("")) return null;
                    org.adapit.wctoolkit.uml.ext.core.Package pk = new org.adapit.wctoolkit.uml.ext.core.Package(root);
                    pk.setLastAsText(str.trim());
                    pk = (org.adapit.wctoolkit.uml.ext.core.Package) pk.getParentElement().addElement(pk);
                    Stereotype st = new Stereotype(pk);
                    st.setName("ModelPackage");
                    pk.assignStereotype(st);
                    modelPackage = pk;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return modelPackage;
    }

    private static org.adapit.wctoolkit.uml.ext.core.Package modelPackage = null;

    public static boolean isModelPackageAvailable() {
        if (modelPackage == null) {
            Model root = DefaultApplicationFrame.getInstance().getSelectedModel();
            List<org.adapit.wctoolkit.uml.ext.core.Package> list = root.getElements(org.adapit.wctoolkit.uml.ext.core.Package.class);
            for (org.adapit.wctoolkit.uml.ext.core.Package pk : list) {
                if (pk.containsStereotype("ModelPackage")) {
                    modelPackage = pk;
                    break;
                }
            }
            return modelPackage != null;
        } else return true;
    }

    public static String getModelPackageNasmespace() {
        return modelPackage.getNamespaceWithoutModel().replace("::", ".");
    }
}
