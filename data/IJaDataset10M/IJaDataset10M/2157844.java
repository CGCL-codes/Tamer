package util;

import dependenceAnalyzer.*;
import java.util.Enumeration;
import java.util.Vector;

public class DepgenUtil extends LinkerUtil implements dependenceAnalyzer.MemberArcTypes {

    protected ClassFileFinder finder = new ClassFileFinder();

    protected ClassnameFilter filter = new ClassnameFilter();

    protected ClassnameFilter wholeClass;

    protected ClassnameFilter wholeData;

    protected MemberDependenceAnalyzer analyzer = new MemberDependenceAnalyzer(finder, filter);

    protected MemberName stringToName(String nameAndType) {
        int nameDivision = methodOff(nameAndType);
        String classname = nameAndType.substring(0, nameDivision);
        String membername = nameAndType.substring(nameDivision + 1);
        return new MemberName(analyzer.classByName(sanitizeClassname(classname)), sanitizeClassname(membername));
    }

    protected MemberDependenceNode stringToNode(String nameAndType) {
        return (MemberDependenceNode) (analyzer.addNodeByName(stringToName(nameAndType)));
    }

    protected MemberName stringToConstructorName(String classAndType) {
        int sigDivision = sigOff(classAndType);
        String classname;
        String signature;
        if (sigDivision == -1) {
            classname = classAndType;
            signature = constructorSig;
        } else {
            classname = classAndType.substring(0, sigDivision);
            signature = classAndType.substring(sigDivision);
        }
        return new MemberName(analyzer.classByName(sanitizeClassname(classname)), constructorName + sanitizeClassname(signature));
    }

    protected MemberDependenceNode stringToConstructorNode(String classAndType) {
        return (MemberDependenceNode) (analyzer.addNodeByName(stringToConstructorName(classAndType)));
    }

    protected ClassEntry stringToClass(String classname) {
        return analyzer.classByName(sanitizeClassname(classname));
    }

    protected void excludeClass(String xclassname) {
        String classname = sanitizeClassname(xclassname);
        filter.includeName(classname);
    }

    protected void processNode(DependenceNode node, int inclusionFlag, int initIncludedFlag, int requiredMask, Vector targetlist, Vector worklist) {
        if (node.state() == DependenceNode.UNANALYZED) analyzer.analyzeDependences(node);
        if (node.state() == DependenceNode.ERROR) return;
        if ((node.flags & MemberDependenceNode.EXCLUDED) != 0) return;
        targetlist.addElement(node);
        node.flags |= inclusionFlag;
        if (node instanceof ClassEntry) {
            ClassEntry classNode = (ClassEntry) node;
            MemberDependenceNode clinit = classNode.lookupMember(staticInitializerName + staticInitializerSig);
            if (clinit != null) {
                if ((clinit.flags & requiredMask) == 0) worklist.addElement(clinit);
            }
            ClassEntry mySuper = classNode.superClass();
            if (mySuper != null) {
                if ((mySuper.flags & requiredMask) == 0) worklist.addElement(mySuper);
            }
            Enumeration ilist = classNode.interfaces();
            while (ilist.hasMoreElements()) {
                ClassEntry iface = (ClassEntry) (ilist.nextElement());
                if ((iface.flags & requiredMask) == 0) worklist.addElement(iface);
            }
            String className = (String) (classNode.name());
            boolean wantAllMembers = wholeClass.accept(null, className);
            boolean wantAllData = wantAllMembers || wholeData.accept(null, className);
            if (wantAllData) {
                Enumeration members = classNode.members();
                if (members != null) {
                    while (members.hasMoreElements()) {
                        MemberDependenceNode mnode = (MemberDependenceNode) (members.nextElement());
                        if (wantAllMembers || ((mnode.flags & MemberDependenceNode.FIELD) != 0)) {
                            if ((mnode.flags & requiredMask) == 0) {
                                worklist.addElement(mnode);
                            }
                        }
                    }
                }
            }
            return;
        }
        MemberDependenceNode mNode = (MemberDependenceNode) node;
        if ((mNode.flags & MemberDependenceNode.OVERRIDDEN) != 0) {
            Enumeration overriders = mNode.overriddenBy();
            while (overriders.hasMoreElements()) {
                MemberDependenceNode t = (MemberDependenceNode) (overriders.nextElement());
                if ((t.flags & requiredMask) != 0) continue;
                if ((((MemberName) (t.name())).classEntry.flags & initIncludedFlag) != 0) {
                    worklist.addElement(t);
                }
            }
        } else if ((mNode.flags & MemberDependenceNode.INIT) != 0) {
            ClassEntry mClass = ((MemberName) (mNode.name())).classEntry;
            if (((mClass.flags & initIncludedFlag) == 0) && (mClass.flags & ClassEntry.HAS_OVERRIDING) != 0) {
                Enumeration mMembers = mClass.members();
                while (mMembers.hasMoreElements()) {
                    MemberDependenceNode t = (MemberDependenceNode) (mMembers.nextElement());
                    if ((t.flags & (MemberDependenceNode.NO_OVERRIDING | MemberDependenceNode.EXCLUDED)) != 0) continue;
                    Enumeration tover = t.overrides();
                    overriders: while (tover.hasMoreElements()) {
                        MemberDependenceNode u = (MemberDependenceNode) (tover.nextElement());
                        if ((u.flags & requiredMask) != 0) {
                            worklist.addElement(t);
                            break overriders;
                        }
                    }
                }
                mClass.flags |= initIncludedFlag;
            }
        }
        Enumeration e = node.dependsOn();
        while (e.hasMoreElements()) {
            DependenceNode t = ((DependenceArc) (e.nextElement())).to();
            if ((t.flags & requiredMask) == 0) {
                worklist.addElement(t);
            }
        }
    }

    protected Vector processList(Vector worklist, int inclusionFlag, int initIncludedFlag, int requiredMask, int verbosity) {
        Vector target = new Vector();
        int targetsize = 0;
        while (worklist.size() != 0) {
            Enumeration working = worklist.elements();
            Vector newlist = new Vector();
            while (working.hasMoreElements()) {
                DependenceNode node = (DependenceNode) (working.nextElement());
                if ((node.flags & requiredMask) == 0) {
                    processNode(node, inclusionFlag, initIncludedFlag, requiredMask, target, newlist);
                }
            }
            int newsize = target.size();
            if (verbosity > 1) {
                System.out.println(Localizer.getString("depgenutil.iteration_added_new_elements", new Integer(newsize - targetsize)));
            }
            targetsize = newsize;
            worklist = newlist;
        }
        return target;
    }

    protected void clearUserFlags(int flagset) {
        int flagmask = ~flagset;
        Enumeration classlist = analyzer.allClasses();
        while (classlist.hasMoreElements()) {
            ClassEntry c = (ClassEntry) (classlist.nextElement());
            c.flags &= flagmask;
            Enumeration memberlist = c.members();
            if (memberlist == null) continue;
            while (memberlist.hasMoreElements()) {
                DependenceNode m = (DependenceNode) (memberlist.nextElement());
                m.flags &= flagmask;
            }
        }
    }
}
