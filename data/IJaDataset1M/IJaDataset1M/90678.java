package ch.sahits.ant;

/**
 * This represents the ant zip element:
 * {@code <zip destfile="manual.zip" basedir="htdocs/manual" update="true" />}
 * @author Andi Hotz, Sahits GmbH
 */
public interface IZip extends IAntElement {

    /**
	 * Retrieve the destination file
	 * @return destination file
	 */
    public String getDestFile();

    /**
	 * Retrieve the base directory
	 * @return base directory
	 */
    public String getBaseDir();

    /**
	 * Indicates to include files only
	 * @return flag
	 */
    public boolean filesonly();

    /**
	 * Defains the behaviour whe nthe archive is empty
	 * @return behaviour
	 */
    public String getWhenEmpty();

    /**
	 * Indicates if the zip archive should be updated
	 * @return flag
	 */
    public boolean update();
}
