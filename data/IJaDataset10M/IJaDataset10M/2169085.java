package JCL.OLD;

import JCL.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Literal representation of a CSP for the Java Constraint Library.
 *
 * This representation implicitly consider the constraints as symmetric and
 * each variable as consistent with itself. The generated Network however
 * is fully explicit.
 *
 * This class knows variables and values orders.
 *
 * @author Erik Bruchez
 */
public class LiteralNetwork {

    String name;

    String author;

    LiteralItemsList domains;

    LiteralItemsList variables;

    LiteralConstraint constraint;

    public LiteralNetwork() {
        domains = new LiteralItemsList();
        variables = new LiteralItemsList();
        constraint = new LiteralConstraint();
    }

    /**
   *	Build a network from the literal network.
   */
    public Network BuildNetwork() {
        Network network = new Network(name, author, CountVariables());
        Hashtable real_domains = new Hashtable(CountDomains());
        for (Enumeration e = GetDomains(); e.hasMoreElements(); ) {
            String dname = (String) (e.nextElement());
            Domain d = new Domain(dname, CountValues(dname));
            int i = 0;
            for (Enumeration f = GetValues(dname); f.hasMoreElements(); i++) d.SetValueName(i, (String) (f.nextElement()));
            real_domains.put(dname, d);
        }
        Hashtable real_variables = new Hashtable(CountVariables());
        int i = 0;
        for (Enumeration e = GetVariables(); e.hasMoreElements(); i++) {
            String vname = (String) (e.nextElement());
            String dname = (String) (GetVariableDomain(vname));
            Domain d = (Domain) (real_domains.get(dname));
            Variable v = new Variable(vname, d);
            real_variables.put(vname, v);
            network.SetVariable(i, v);
        }
        i = 0;
        int implicit = 0;
        int explicit = 0;
        for (Enumeration e = GetVariables(); e.hasMoreElements(); i++) {
            String vname1 = (String) (e.nextElement());
            int j = 0;
            boolean accept_var = false;
            for (Enumeration f = GetVariables(); f.hasMoreElements(); j++) {
                String vname2 = (String) (f.nextElement());
                if (!accept_var) {
                    if (vname1.equals(vname2)) accept_var = true; else continue;
                }
                Variable v1 = (Variable) (real_variables.get(vname1));
                Variable v2 = (Variable) (real_variables.get(vname2));
                Constraint c1 = new Constraint(v1, v2);
                Constraint c2 = new Constraint(v2, v1);
                int a = 0;
                boolean Implicit = true;
                int count = 0;
                for (Enumeration g = GetValues(GetVariableDomain(vname1)); g.hasMoreElements(); a++) {
                    String valname1 = (String) (g.nextElement());
                    int b = 0;
                    for (Enumeration h = GetValues(GetVariableDomain(vname2)); h.hasMoreElements(); b++) {
                        String valname2 = (String) (h.nextElement());
                        if (i == j) {
                            if (a == b) c1.SetConstraint(valname1, valname2);
                        } else if (CheckConstraint(vname1, vname2, valname1, valname2)) {
                            c1.SetConstraint(valname1, valname2);
                            c2.SetConstraint(valname2, valname1);
                            Implicit = false;
                            count++;
                        }
                    }
                }
                if (i == j) {
                    c1.SetImplicitConstraint();
                    network.SetConstraint(c1);
                    implicit++;
                } else {
                    if (!Implicit) {
                        network.SetConstraint(c1);
                        network.SetConstraint(c2);
                        explicit++;
                        explicit++;
                    } else {
                        c1.SetImplicitConstraint();
                        c2.SetImplicitConstraint();
                        network.SetConstraint(c1);
                        network.SetConstraint(c2);
                        implicit++;
                        implicit++;
                    }
                }
            }
        }
        return network;
    }

    /**
   *	Predicate to ask if the literal network is empty.
   */
    public boolean Empty() {
        if (CountVariables() == 0) return true; else return false;
    }

    /**
   *	Build a string with the network contents.
   */
    public String toString() {
        String s = new String("");
        s += "Network\n";
        if ((name != null) && (!name.equals(""))) {
            s += "\tName : " + name + "\n";
        }
        if ((author != null) && (!author.equals(""))) {
            s += "\tAuthor : " + author + "\n";
        }
        s += "Domains\n";
        for (Enumeration e = GetDomains(); e.hasMoreElements(); ) {
            String dname = (String) (e.nextElement());
            int vcount = CountValues(dname);
            s += "\t" + dname + " : ";
            int i = 0;
            for (Enumeration f = GetValues(dname); f.hasMoreElements(); i++) {
                String vname = (String) (f.nextElement());
                s += vname;
                if (i != (vcount - 1)) s += ", "; else s += "\n";
            }
        }
        s += "Variables\n";
        for (Enumeration e = GetVariables(); e.hasMoreElements(); ) {
            String vname = (String) (e.nextElement());
            String dname = null;
            try {
                dname = GetVariableDomain(vname);
            } catch (LiteralItemsListException ex) {
            }
            s += "\t" + vname + " : ";
            if (dname != null) s += dname;
            s += "\n";
        }
        s += "Constraints\n";
        for (Enumeration e = GetVariables(); e.hasMoreElements(); ) {
            String vname1 = (String) (e.nextElement());
            boolean accept_var = false;
            for (Enumeration f = GetVariables(); f.hasMoreElements(); ) {
                String vname2 = (String) (f.nextElement());
                if (!accept_var) {
                    if (vname1.equals(vname2)) accept_var = true;
                    continue;
                }
                int constraints = 0;
                for (Enumeration g = GetValues(GetVariableDomain(vname1)); g.hasMoreElements(); ) {
                    String valname1 = (String) (g.nextElement());
                    for (Enumeration h = GetValues(GetVariableDomain(vname2)); h.hasMoreElements(); ) {
                        String valname2 = (String) (h.nextElement());
                        if (CheckConstraint(vname1, vname2, valname1, valname2)) {
                            if (constraints == 0) s += "\t" + vname1 + ", " + vname2 + " : "; else s += ", ";
                            constraints++;
                            s += "(" + valname1 + ", " + valname2 + ")";
                        }
                    }
                }
                if (constraints > 0) s += "\n";
            }
        }
        return s;
    }

    /**
   *	Set network name.
   */
    public void SetName(String name) {
        this.name = new String(name);
    }

    /**
   *	Get network name.
   */
    public String GetName() {
        return new String(this.name);
    }

    /**
   *	Set network author.
   */
    public void SetAuthor(String author) {
        this.author = new String(author);
    }

    /**
   *	Get network author.
   */
    public String GetAuthor() {
        return new String(this.author);
    }

    /**
   *	Add a new domain.
   */
    public void AddDomain(String name) {
        domains.AddItem(name, new LiteralItemsList());
    }

    /**
   *	Remove an existing domain.
   */
    public void RemoveDomain(String name) {
        domains.RemoveItem(name);
        constraint.RemoveDomain(name);
    }

    /**
   *	Rename an existing domain.
   */
    public void RenameDomain(String old_name, String new_name) {
        domains.RenameItem(old_name, new_name);
        for (Enumeration e = variables.GetItems(); e.hasMoreElements(); ) {
            String variable = (String) (e.nextElement());
            String domain = (String) (variables.GetObject(variable));
            if (domain.equals(old_name)) {
                variables.SetObject(variable, new String(new_name));
            }
        }
        constraint.RenameDomain(old_name, new_name);
    }

    /**
   *	Check if an item exists.
   */
    public boolean CheckDomain(String name) {
        return domains.CheckItem(name);
    }

    /**
   *	Count the number of domains.
   */
    public int CountDomains() {
        return domains.CountItems();
    }

    /**
   *	Return an enumeration of the domains names.
   */
    public Enumeration GetDomains() {
        return domains.GetItems();
    }

    /**
   *	Add a new value to an existing domain.
   */
    public void AddValue(String dname, String vname) {
        LiteralItemsList l = (LiteralItemsList) (domains.GetObject(dname));
        l.AddItem(vname);
    }

    /**
   *	Remove an existing value from an existing domain.
   */
    public void RemoveValue(String dname, String vname) {
        LiteralItemsList l = (LiteralItemsList) (domains.GetObject(dname));
        l.RemoveItem(vname);
        constraint.RemoveValue(dname, vname);
    }

    /**
   *	Rename an existing value of an existing domain.
   */
    public void RenameValue(String dname, String old_vname, String new_vname) {
        LiteralItemsList l = (LiteralItemsList) (domains.GetObject(dname));
        l.RenameItem(old_vname, new_vname);
        constraint.RenameValue(dname, old_vname, new_vname);
    }

    /**
   *	Check if a value exists.
   */
    public boolean CheckValue(String dname, String vname) {
        LiteralItemsList l = (LiteralItemsList) (domains.GetObject(dname));
        return l.CheckItem(vname);
    }

    /**
   *	Count the number of values of an existing domain.
   */
    public int CountValues(String dname) {
        LiteralItemsList l = (LiteralItemsList) (domains.GetObject(dname));
        return l.CountItems();
    }

    /**
   *	Return an enumeration of all the values associated with a domain.
   */
    public Enumeration GetValues(String dname) {
        LiteralItemsList l = (LiteralItemsList) (domains.GetObject(dname));
        return l.GetItems();
    }

    /**
   *	Add a new variable to the network.
   */
    public void AddVariable(String name) {
        variables.AddItem(name);
    }

    /**
   *	Remove a variable from the network.
   */
    public void RemoveVariable(String name) {
        variables.RemoveItem(name);
        constraint.RemoveVariable(name);
    }

    /**
   *	Rename an existing variable.
   */
    public void RenameVariable(String old_name, String new_name) {
        variables.RenameItem(old_name, new_name);
        constraint.RenameVariable(old_name, new_name);
    }

    /**
   *	Check if a variable exists.
   */
    public boolean CheckVariable(String name) {
        return variables.CheckItem(name);
    }

    /**
   *	Count the number of variables.
   */
    public int CountVariables() {
        return variables.CountItems();
    }

    /**
   *	Return an enumeration of the variables names.
   */
    public Enumeration GetVariables() {
        return variables.GetItems();
    }

    /**
   *	Return a LiteralItemsList of the variables.
   */
    public LiteralItemsList GetListVariables() {
        return variables;
    }

    private void CheckConstraintParameters(String var1, String var2, String val1, String val2) {
        boolean ok = CheckVariable(var1);
        if (ok == false) throw new UndefinedVariableException();
        ok = CheckVariable(var2);
        if (ok == false) throw new UndefinedVariableException();
        String dname1 = GetVariableDomain(var1);
        String dname2 = GetVariableDomain(var2);
        ok = CheckValue(dname1, val1);
        if (ok == false) throw new UndefinedValueException();
        ok = CheckValue(dname2, val2);
        if (ok == false) throw new UndefinedValueException();
    }

    /**
   *	Set a constraint between two existing variables.
   */
    public void SetConstraint(String var1, String var2, String val1, String val2) {
        CheckConstraintParameters(var1, var2, val1, val2);
        if (var1.equals(var2)) return;
        int index1 = variables.GetItemIndex(var1);
        int index2 = variables.GetItemIndex(var2);
        if (index2 < index1) {
            String var = var1;
            String val = val1;
            var1 = var2;
            val1 = val2;
            var2 = var;
            val2 = val;
        }
        constraint.SetConstraint(var1, var2, val1, val2);
    }

    /**
   *	Unset a constraint between two existing variables.
   */
    public void UnsetConstraint(String var1, String var2, String val1, String val2) {
        CheckConstraintParameters(var1, var2, val1, val2);
        if (var1.equals(var2)) return;
        int index1 = variables.GetItemIndex(var1);
        int index2 = variables.GetItemIndex(var2);
        if (index2 < index1) {
            String var = var1;
            String val = val1;
            var1 = var2;
            val1 = val2;
            var2 = var;
            val2 = val;
        }
        constraint.UnsetConstraint(var1, var2, val1, val2);
    }

    /**
   *	Check if there is a constraint between two existing variables.
   */
    public boolean CheckConstraint(String var1, String var2, String val1, String val2) {
        CheckConstraintParameters(var1, var2, val1, val2);
        if (var1.equals(var2)) {
            if (val1.equals(val2)) return true; else return false;
        }
        int index1 = variables.GetItemIndex(var1);
        int index2 = variables.GetItemIndex(var2);
        if (index2 < index1) {
            String var = var1;
            String val = val1;
            var1 = var2;
            val1 = val2;
            var2 = var;
            val2 = val;
        }
        return constraint.CheckConstraint(var1, var2, val1, val2);
    }

    /**
   *	Unset all constraints between two existing variables.
   */
    public void UnsetAllConstraints() {
        constraint.UnsetAllConstraints();
    }

    /**
   *	Associate a domain to an existing variable.
   */
    public void SetVariableDomain(String variable, String new_domain) {
        String old_domain = GetVariableDomain(variable);
        if (!new_domain.equals(old_domain)) {
            variables.SetObject(variable, new_domain);
            constraint.ChangeVariableDomain(variable, new_domain);
        }
    }

    /**
   *	Get the domain associated with an existing variable.
   */
    public String GetVariableDomain(String variable) {
        return (String) (variables.GetObject(variable));
    }
}

class LiteralConstraint {

    Hashtable variables;

    Hashtable domains;

    Object dummy;

    public LiteralConstraint() {
        variables = new Hashtable();
        domains = new Hashtable();
        dummy = new Object();
    }

    public void RemoveDomain(String name) {
        for (Enumeration e = domains.keys(); e.hasMoreElements(); ) {
            String variable = (String) (e.nextElement());
            String domain = (String) (domains.get(variable));
            if (domain.equals(name)) {
                RemoveAllConstraintsOnVariable(variable);
                domains.remove(variable);
            }
        }
    }

    public void RenameDomain(String old_name, String new_name) {
        for (Enumeration e = domains.keys(); e.hasMoreElements(); ) {
            String variable = (String) (e.nextElement());
            String domain = (String) (domains.get(variable));
            if (domain.equals(old_name)) domains.put(variable, new String(new_name));
        }
    }

    public void SetConstraint(String var1, String var2, String val1, String val2) {
        Hashtable t = (Hashtable) (variables.get(var1));
        if (t == null) {
            t = new Hashtable();
            variables.put(var1, t);
        }
        Hashtable u = (Hashtable) (t.get(var2));
        if (u == null) {
            u = new Hashtable();
            t.put(var2, u);
        }
        Hashtable v = (Hashtable) (u.get(val1));
        if (v == null) {
            v = new Hashtable();
            u.put(val1, v);
        }
        v.put(val2, dummy);
    }

    public void UnsetConstraint(String var1, String var2, String val1, String val2) {
        Hashtable t = (Hashtable) (variables.get(var1));
        Hashtable u = (Hashtable) (t.get(var2));
        Hashtable v = (Hashtable) (u.get(val1));
        v.remove(val2);
    }

    public boolean CheckConstraint(String var1, String var2, String val1, String val2) {
        Hashtable t = (Hashtable) (variables.get(var1));
        if (t == null) return false;
        Hashtable u = (Hashtable) (t.get(var2));
        if (u == null) return false;
        Hashtable v = (Hashtable) (u.get(val1));
        if (v == null) return false;
        Object o = v.get(val2);
        return (o != null) ? true : false;
    }

    public void UnsetAllConstraints() {
        variables.clear();
    }

    private void SetVariableDomain(String variable, String domain) {
        domains.put(variable, new String(domain));
    }

    public void ChangeVariableDomain(String variable, String new_domain) {
        RemoveAllConstraintsOnVariable(variable);
        SetVariableDomain(variable, new_domain);
    }

    public void RemoveVariable(String name) {
        RemoveAllConstraintsOnVariable(name);
        domains.remove(name);
    }

    public void RenameVariable(String old_name, String new_name) {
        for (Enumeration e = variables.keys(); e.hasMoreElements(); ) {
            String s = (String) (e.nextElement());
            Hashtable t = (Hashtable) (variables.get(s));
            if (s.equals(old_name)) {
                variables.remove(s);
                variables.put(new_name, t);
            }
            for (Enumeration f = t.keys(); f.hasMoreElements(); ) {
                String s1 = (String) (f.nextElement());
                if (s1.equals(old_name)) {
                    Object o = t.get(s1);
                    t.remove(s1);
                    t.put(new_name, o);
                }
            }
        }
        for (Enumeration e = domains.keys(); e.hasMoreElements(); ) {
            String variable = (String) (e.nextElement());
            if (variable.equals(old_name)) {
                String domain = (String) (domains.get(variable));
                domains.remove(variable);
                domains.put(new_name, domain);
            }
        }
    }

    public void RemoveValue(String dname, String vname) {
        for (Enumeration e = variables.keys(); e.hasMoreElements(); ) {
            String variable = (String) (e.nextElement());
            String domain = (String) (domains.get(variable));
            if (domain.equals(dname)) {
                Hashtable t = (Hashtable) (variables.get(variable));
                for (Enumeration f = t.elements(); f.hasMoreElements(); ) {
                    Hashtable u = (Hashtable) (f.nextElement());
                    Hashtable v = (Hashtable) (u.get(vname));
                    if (v != null) {
                        v.clear();
                        u.remove(vname);
                    }
                }
            }
        }
        for (Enumeration e = variables.elements(); e.hasMoreElements(); ) {
            Hashtable t = (Hashtable) (e.nextElement());
            for (Enumeration f = t.keys(); f.hasMoreElements(); ) {
                String variable = (String) (f.nextElement());
                String domain = (String) (domains.get(variable));
                if (domain.equals(dname)) {
                    Hashtable u = (Hashtable) (t.get(variable));
                    for (Enumeration g = u.keys(); g.hasMoreElements(); ) {
                        String value = (String) (g.nextElement());
                        Hashtable v = (Hashtable) (u.get(value));
                        Object o = v.get(vname);
                        if (o != null) v.remove(vname);
                        if (v.isEmpty()) u.remove(value);
                    }
                }
            }
        }
    }

    public void RenameValue(String dname, String old_vname, String new_vname) {
        System.out.println("Try to rename " + old_vname + " to " + new_vname);
        System.out.println("Domain : " + dname);
        for (Enumeration e = variables.keys(); e.hasMoreElements(); ) {
            String variable = (String) (e.nextElement());
            String domain = (String) (domains.get(variable));
            System.out.println("variable " + variable + " : domain " + domain);
            if (domain.equals(dname)) {
                System.out.println("Domain " + dname + " found");
                Hashtable t = (Hashtable) (variables.get(variable));
                for (Enumeration f = t.elements(); f.hasMoreElements(); ) {
                    Hashtable u = (Hashtable) (f.nextElement());
                    Hashtable v = (Hashtable) (u.get(old_vname));
                    if (v != null) {
                        u.remove(old_vname);
                        u.put(new_vname, v);
                        System.out.println("1 : " + old_vname + " changed to " + new_vname);
                    }
                }
            }
        }
        for (Enumeration e = variables.elements(); e.hasMoreElements(); ) {
            Hashtable t = (Hashtable) (e.nextElement());
            for (Enumeration f = t.keys(); f.hasMoreElements(); ) {
                String variable = (String) (f.nextElement());
                String domain = (String) (domains.get(variable));
                if (domain.equals(dname)) {
                    Hashtable u = (Hashtable) (t.get(variable));
                    for (Enumeration g = u.keys(); g.hasMoreElements(); ) {
                        String value = (String) (g.nextElement());
                        Hashtable v = (Hashtable) (u.get(value));
                        Object o = v.get(old_vname);
                        if (o != null) {
                            v.remove(old_vname);
                            v.put(new_vname, o);
                        }
                    }
                }
            }
        }
    }

    private void RemoveAllConstraintsOnVariable(String name) {
        Hashtable t = (Hashtable) (variables.get(name));
        if (t != null) {
            for (Enumeration e = t.elements(); e.hasMoreElements(); ) {
                Hashtable u = (Hashtable) (e.nextElement());
                for (Enumeration f = u.elements(); f.hasMoreElements(); ) {
                    Hashtable v = (Hashtable) (f.nextElement());
                    v.clear();
                }
                u.clear();
            }
            t.clear();
            variables.remove(name);
        }
        for (Enumeration e = variables.elements(); e.hasMoreElements(); ) {
            Hashtable t1 = (Hashtable) (e.nextElement());
            for (Enumeration f = t1.keys(); f.hasMoreElements(); ) {
                String s = (String) (f.nextElement());
                if (s.equals(name)) {
                    Hashtable u = (Hashtable) (t1.get(s));
                    for (Enumeration g = u.elements(); g.hasMoreElements(); ) {
                        Hashtable v = (Hashtable) (g.nextElement());
                        v.clear();
                    }
                    u.clear();
                    t1.remove(name);
                }
            }
        }
    }
}
