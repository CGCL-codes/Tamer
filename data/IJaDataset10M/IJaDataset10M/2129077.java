package Sale;

/**
  * A name context.
  * 
  * <p>Name contexts are a policy to shield rename functions. You can think of a name context as a name space that
  * comes with certain rules that decide validity of names. One rule could be, for example, that names must be
  * unique in a name context.</p>
  *
  * @author Steffen Zschaler
  * @version 0.5
  */
public interface NameContext {

    /**
    * Check whether <i>sName</i> complies with the rules of this NameContext.
    *
    * @param sName the name to be checked.
    *
    * @return true, if <i>sName</i> complies with the rules.
    */
    public boolean isValidName(String sName);

    /**
    * Inform the name context of a name change that occurred. This should adjust any additional tables that rely
    * on the names of objects in this name context.
    */
    public void nameHasChanged(String oldName, String newName);

    /**
    * Return a locker object that can be used to gain exclusive access to the name context.
    */
    public Object getLockObject();
}
