package uk.ac.cisban.saint.client.structure;

import org.junit.Before;
import org.junit.Test;
import uk.ac.cisban.saint.client.MiriamService;

/**
 *
 * This file is part of the Saint package.
 * Saint is covered under the GNU Lesser General Public License (LGPL).
 * <p/>
 * Copyright (C) 2009, Allyson Lister, Morgan Taschuk and their employers
 * <p/>
 * Modifications to the initial code base are copyright of their respective authors, or their employers as appropriate.
 * Authorship of the modifications may be determined from the Subversion history.
 * <p/>
 * To view the full licensing information for this software and ALL other packages contained in this distribution,
 * please see LICENSE.txt
 * 
 */
@SuppressWarnings({ "NonJREEmulationClassesInClientCode" })
public class ConstantsConverterTest {

    private ConstantsConverter converter;

    @Before
    public void setUp() {
        MiriamService miriam = new uk.ac.cisban.saint.server.MiriamServiceImpl();
        converter = ConstantsConverter.getInstance();
    }

    @Test
    public void getUsefulUrlTest() {
        String response = converter.getUsefulUri("urn:miriam:obo.go:GO%3A0000781");
        org.junit.Assert.assertEquals("http://www.ebi.ac.uk/ego/GTerm?id=GO%3A0000781", response);
    }
}
