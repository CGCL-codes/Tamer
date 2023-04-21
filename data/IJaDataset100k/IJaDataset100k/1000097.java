package org.objectwiz.test;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
import org.objectwiz.entity.*;
import org.junit.Before;
import javax.persistence.*;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Test;
import org.objectwiz.entityDAO.ClientJpaController;
import org.objectwiz.entityDAO.InterventionJpaController;
import static org.junit.Assert.*;
import org.objectwiz.entityDAO.CompanyJpaController;
import org.objectwiz.entityDAO.TechnicianJpaController;
import org.objectwiz.entityDAO.exceptions.NonexistentEntityException;
import org.objectwiz.function.ManipulateEntity;

/**
 *
 * @author xym
 */
public class ObjectwizTest {

    @Test
    public final void testA() throws NonexistentEntityException, Exception {
    }
}
