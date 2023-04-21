package net.sf.mailsomething.filter;

/**
 * 
 * @author Louise Miller
 *
 * Sep 9, 2003
 */
public interface Filter {

    /**
	 * Call this apply and let it have another signature -
	 * Object[] apply(Object[])
	 * 
	 * Explenation - 
	 * 1. A filter can manipulate an object. 
	 * 2. Some classes using the filter might want to have more objects
	 * in argument. 
	 * 3. I prefer to call it apply since it gives an idea that something
	 * is happening. 
	 * 
	 * BUT perhabs this method I describe here is for another type of filter.
	 * I think theres basicly 2 kinds -
	 * a filter which lies it self upon some output, which will be the input
	 * of the filter, and then outputs something else. For example
	 * sun - sunrayes - filter (sunglasses) - changes sunrayes
	 * When u remove the filter the output will be as the original output
	 * And a filter which is part of the action somehow. 
	 * pees + water - filter - water
	 * This filter is 'unredoable'
	 * And I think the filter u done is the last one, while the filter for
	 * a mailboxtable or some other guicomponent should be of the first one. 
	 * 
	 * Maybe theres actually no difference, since u cant get the original
	 * sunrayes back. Its only a percieved difference, since the sunrayes
	 * seems the same before and after.  
	 * 
	 * @param obj
	 * @return
	 */
    public boolean applies(Object obj);

    public FilterAction getAction();

    /**
	 * I like to have the criterias in another object. Ie the 3 methods
	 * below should be placed in some object called...  FilterDefinition
	 * or similar. 
	 * 
	 * 
	 * @return
	 */
    public Criteria[] getCriteria();

    public void addCriteria(Criteria criteria);

    public void removeCriteria(Criteria criteria);

    /**
	 * This and getname are GUI methods and should not be here. 
	 * 
	 * @param name
	 */
    public void setName(String name);

    public String getName();

    public void doAction(Object obj);
}
