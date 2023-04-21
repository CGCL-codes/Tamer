package net.sf.dict4j.entity;

import java.io.Serializable;

public interface Database extends Serializable {

    /**
	 * If the database name is specified with a star (decimal code 42, "*"),
	 * then all of the matches in all available databases will be displayed.
	 */
    String ALL_DATABASES = "*";

    /**
	 * If the database name is specified with an exclamation point (decimal code 33, "!"),
	 * then all of the databases will be searched until a match is found,
	 * and all matches in that database will be displayed.
	 */
    String FIRST_DATABASE = "!";

    String getDescription();

    String getName();
}
