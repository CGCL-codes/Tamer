package com.darkhonor.rage.model;

import com.darkhonor.rage.model.exceptions.DAOException;

/**
 * An Abstract DAO Factory used by RAGE.
 *
 * @author Alex Ackerman
 */
public abstract class DAOFactory {

    public static final int JPA = 1;

    public static final int HIBERNATE = 2;

    public static final int JDBC = 3;

    public abstract CourseDAO getCourseDAO() throws DAOException;
}
