package org.freelords.game;

import java.io.IOException;
import org.freelords.armies.Army;
import org.freelords.armies.Hero;
import org.freelords.buildings.City;
import org.freelords.buildings.Ruin;
import org.freelords.buildings.Temple;
import org.freelords.entity.FreelordsMapEntity;
import org.freelords.util.io.loaders.VirtualPath;
import org.freelords.xml.XMLAnnotatedHandlerFactory;
import org.freelords.xml.XMLHelper;
import org.freelords.xml.XMLTestHelper;
import org.freelords.xml.standard.XMLSubClassHandler;
import org.junit.Before;
import org.junit.Test;

/**
  * Tests classes EntityGrouping and ScenarioEntities
  *
  * @author James Andrews
  */
public class EntityXMLTest extends XMLTestHelper {

    /** Make an entity group with an army, hero, ... */
    @Before
    public void setup() {
        XMLHelper xmlHelper = getXMLHelper();
        xmlHelper.addHandler(EntityGrouping.class, EntityGrouping.groupHandler);
        xmlHelper.addHandler(FreelordsMapEntity.class, new XMLSubClassHandler<FreelordsMapEntity>().addSubClass("army", Army.class, XMLAnnotatedHandlerFactory.XML_CONSTRUCTABLE_HANDLER.generateXMLHandler(Army.class)).addSubClass("hero", Hero.class, XMLAnnotatedHandlerFactory.XML_CONSTRUCTABLE_HANDLER.generateXMLHandler(Hero.class)).addSubClass("temple", Temple.class, XMLAnnotatedHandlerFactory.XML_CONSTRUCTABLE_HANDLER.generateXMLHandler(Temple.class)).addSubClass("ruin", Ruin.class, XMLAnnotatedHandlerFactory.XML_CONSTRUCTABLE_HANDLER.generateXMLHandler(Ruin.class)).addSubClass("city", City.class, XMLAnnotatedHandlerFactory.XML_CONSTRUCTABLE_HANDLER.generateXMLHandler(City.class)));
    }

    /** A test */
    @Test
    public void testTwoWay() throws IOException {
        biTest(new VirtualPath("EntityGroupingXMLTest_data_1.xml"), EntityGrouping.class);
        biTest(new VirtualPath("ScenarioEntitiesXMLTest_data_1.xml"), ScenarioEntities.class);
    }
}
