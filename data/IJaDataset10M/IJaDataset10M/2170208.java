package dex.compiler.model.statement;

import java.util.List;
import dex.compiler.model.base.Place;

/**
 * A try/catch/finally statement.
 */
public class TryStatement extends Statement {

    /**
	 * The try section.
	 */
    private Block trySection;

    /**
	 * The catch section.
	 */
    private List<CatchBlock> catchSection;

    /**
	 * The finally section.
	 */
    private Block finallySection;

    /**
	 * Constructs a new try/catch/finally statement.
	 * 
	 * @param place  the place where the try/catch/finally originated
	 * @param trySection  the try section
	 * @param catchSection  the catch section  
	 * @param finallySection  the finally section, or null if no 
	 *    finally section is specified
	 */
    public TryStatement(Place place, Block trySection, List<CatchBlock> catchSection, Block finallySection) {
        super(place);
        if (trySection == null) {
            throw new IllegalArgumentException("trySection may not be null");
        }
        if (catchSection == null) {
            throw new IllegalArgumentException("catchSection may not be null");
        }
        if (catchSection.size() == 0) {
            throw new IllegalArgumentException("At least one catch block is required.");
        }
        this.trySection = trySection;
        this.catchSection = catchSection;
        this.finallySection = finallySection;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("try\n");
        result.append(trySection);
        for (CatchBlock cb : catchSection) {
            result.append(cb);
        }
        if (finallySection != null) {
            result.append("finally\n").append(finallySection);
        }
        return result.toString();
    }

    /**
	 * Returns the catch section of this try/catch/finally statement.
	 * 
	 * @return  the catch section
	 */
    public List<CatchBlock> getCatchSection() {
        return catchSection;
    }

    /**
	 * Returns the finally section of this try/catch/finally statement.
	 * This method may return null if the finally section was not specified.
	 * 
	 * @return  the finally section.
	 */
    public Block getFinallySection() {
        return finallySection;
    }

    /**
	 * Returns the try section of this try/catch/finally statement.
	 * 
	 * @return  the try section
	 */
    public Block getTrySection() {
        return trySection;
    }

    @Override
    public boolean terminates() {
        if (finallySection != null) {
            if (finallySection.terminates()) {
                return true;
            }
        }
        if (!trySection.terminates()) {
            return false;
        }
        for (CatchBlock c : catchSection) {
            if (!c.getBlock().terminates()) {
                return false;
            }
        }
        return true;
    }
}
