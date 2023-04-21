package designPatterns.gof.creational.builder;

/**
 * The interface <code>Product</code> defines interface to create parts of the Product.
 * 
 * @pattern Builder (role=builderType)
 * 
 * @generatedBy CodePro at 12/4/07 7:04 PM
 * 
 * @author Sandeep.Maloth
 * 
 * @version $Revision$
 */
public interface Builder {

    /**
	 * Construct part of the complex Product.
	 */
    public void buildPart();

    /**
	 * Construct the Product.
	 * 
	 * @return the constructed product
	 */
    public Product getProduct();
}
