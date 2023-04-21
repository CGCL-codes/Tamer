package alefpp.core;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

/**
 * A Base of Modules in Alef++
 * @author Adrabi Abderrahim
 * @version 1.5
 */
public class AModule extends Scope {

    private String moduleName = null;

    private String packageName = "alefpp.parser";

    private Hashtable<String, AStatement> children = null;

    private Hashtable<String, Stack<AStatement>> subroutines = null;

    /**
	 * Constructor with module name
	 * @param $1
	 */
    public AModule(String $1) {
        this.moduleName = $1;
        this.children = new Hashtable<String, AStatement>();
        this.subroutines = new Hashtable<String, Stack<AStatement>>();
    }

    /**
	 * Constructor with module name and package name 
	 * @param $1
	 * @param $2
	 */
    public AModule(String $1, String $2) {
        this.moduleName = $1;
        this.packageName = $2;
        this.children = new Hashtable<String, AStatement>();
        this.subroutines = new Hashtable<String, Stack<AStatement>>();
    }

    /**
	 * Module name
	 * @return
	 */
    public String getModuleName() {
        return this.moduleName;
    }

    /**
	 * Package name
	 * @return
	 */
    public String getPackageName() {
        return this.packageName;
    }

    public AStatement parent() {
        throw new java.lang.UnsupportedOperationException();
    }

    public boolean parent(AStatement $1) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean child(String $1, AStatement $2) {
        $2.parent(this);
        return this.children.put($1, $2) != null;
    }

    @Override
    public AStatement child(String $1) {
        return this.children.get($1);
    }

    @Override
    public boolean existsChild(String $1) {
        return this.children.containsKey($1);
    }

    public boolean subroutine(String $1, AStatement $2) {
        if (this.existsSubroutine($1)) {
            Stack<AStatement> tmp = this.subroutines.get($1);
            return tmp.add($2);
        }
        Stack<AStatement> tmp = new Stack<AStatement>();
        tmp.add($2);
        return this.subroutines.put($1, tmp) != null;
    }

    public Enumeration subroutines(String $1) {
        return this.subroutines.get($1).elements();
    }

    public boolean existsSubroutine(String $1) {
        return this.subroutines.containsKey($1);
    }
}
