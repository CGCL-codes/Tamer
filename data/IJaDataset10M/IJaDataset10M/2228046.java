package org.aspencloud.simple9.tests.integration;

import java.util.Iterator;
import java.util.Set;
import org.aspencloud.simple9.persist.persistables.Persistable;
import org.aspencloud.simple9.server.annotations.Attribute;
import org.aspencloud.simple9.server.annotations.Model;
import org.aspencloud.simple9.server.annotations.Relation;
import org.aspencloud.simple9.tests.TestBase;
import org.aspencloud.simple9.tests.TestModel;

public class TestHasMany extends TestBase {

    private TestModel car;

    private TestModel driver;

    @SuppressWarnings("unchecked")
    public void testOneWay() throws Exception {
        String a1 = "@" + Model.class.getSimpleName() + "(\n" + " attrs = {\n" + "  @" + Attribute.class.getSimpleName() + "(type=String.class, name=\"name\")\n" + " },\n" + " hasOne = {\n" + "  @" + Relation.class.getSimpleName() + "(Driver.class)\n" + " }\n" + ")";
        car = new TestModel("Car", a1);
        String a2 = "@" + Model.class.getSimpleName() + "(\n" + " attrs = {\n" + "  @" + Attribute.class.getSimpleName() + "(type=String.class, name=\"name\")\n" + " },\n" + " hasMany = {\n" + "  @" + Relation.class.getSimpleName() + "(Car.class)\n" + " }\n" + ")";
        driver = new TestModel("Driver", a2);
        addModel(car);
        addModel(driver);
        createModels();
        migrate();
        Persistable d = driver.newInstance(server);
        Persistable c1 = car.newInstance(server);
        get(d, "cars", Set.class).add(c1);
        set(c1, "name", "Car1");
        d.save();
        int id = d.getId();
        assertTrue(id > 0);
        server.clearReferences();
        d = server.persistor().find(driver.getModelClass(), id);
        assertNotNull(d);
        Set cars = get(d, "cars", Set.class);
        assertEquals(1, cars.size());
        assertEquals("Car1", get(cars.iterator().next(), "name"));
        router.addRoute(driver.getModelClass());
        router.addRoute(car.getModelClass());
        d = client.persistor().find(driver.getModelClass(), id);
        assertNotNull(d);
        cars = get(d, "cars", Set.class);
        assertEquals(1, cars.size());
        assertEquals("Car1", get(cars.iterator().next(), "name"));
        Persistable c2 = car.newInstance(client);
        set(c2, "name", "Car2");
        cars.add(c2);
        client.clearReferences();
        server.clearReferences();
        d = client.persistor().find(driver.getModelClass(), id);
        assertNotNull(d);
        cars = get(d, "cars", Set.class);
        assertEquals(2, cars.size());
        Iterator<?> iter = cars.iterator();
        assertEquals("Car1", get(iter.next(), "name"));
        assertEquals("Car2", get(iter.next(), "name"));
    }

    @SuppressWarnings("unchecked")
    public void testTwoWay() throws Exception {
        String a1 = "@" + Model.class.getSimpleName() + "(\n" + " attrs = {\n" + "  @" + Attribute.class.getSimpleName() + "(type=String.class, name=\"name\")\n" + " },\n" + " hasOne = {\n" + "  @" + Relation.class.getSimpleName() + "(type=Driver.class, name=\"driver\", opposite=\"cars\")\n" + " }\n" + ")";
        car = new TestModel("Car", a1);
        String a2 = "@" + Model.class.getSimpleName() + "(\n" + " attrs = {\n" + "  @" + Attribute.class.getSimpleName() + "(type=String.class, name=\"name\")\n" + " },\n" + " hasMany = {\n" + "  @" + Relation.class.getSimpleName() + "(type=Car.class, name=\"cars\", opposite=\"driver\")\n" + " }\n" + ")";
        driver = new TestModel("Driver", a2);
        addModel(car);
        addModel(driver);
        createModels();
        migrate();
        Persistable d = driver.newInstance(server);
        Persistable c1 = car.newInstance(server);
        get(d, "cars", Set.class).add(c1);
        set(c1, "name", "Car1");
        d.save();
        int id = d.getId();
        assertTrue(id > 0);
        server.clearReferences();
        d = server.persistor().find(driver.getModelClass(), id);
        assertNotNull(d);
        Set cars = get(d, "cars", Set.class);
        assertEquals(1, cars.size());
        assertEquals("Car1", get(cars.iterator().next(), "name"));
        router.addRoute(driver.getModelClass());
        router.addRoute(car.getModelClass());
        d = client.persistor().find(driver.getModelClass(), id);
        assertNotNull(d);
        cars = get(d, "cars", Set.class);
        assertEquals(1, cars.size());
        assertEquals("Car1", get(cars.iterator().next(), "name"));
        Persistable c2 = car.newInstance(client);
        set(c2, "name", "Car2");
        cars.add(c2);
        client.clearReferences();
        server.clearReferences();
        d = client.persistor().find(driver.getModelClass(), id);
        assertNotNull(d);
        cars = get(d, "cars", Set.class);
        assertEquals(2, cars.size());
        Iterator<?> iter = cars.iterator();
        assertEquals("Car1", get(iter.next(), "name"));
        assertEquals("Car2", get(iter.next(), "name"));
    }

    @SuppressWarnings("unchecked")
    public void testTwoWayHasMany() throws Exception {
        String a1 = "@" + Model.class.getSimpleName() + "(\n" + " attrs = {\n" + "  @" + Attribute.class.getSimpleName() + "(type=String.class, name=\"name\")\n" + " },\n" + " hasMany = {\n" + "  @" + Relation.class.getSimpleName() + "(type=Driver.class, name=\"drivers\", opposite=\"cars\")\n" + " }\n" + ")";
        car = new TestModel("Car", a1);
        String a2 = "@" + Model.class.getSimpleName() + "(\n" + " attrs = {\n" + "  @" + Attribute.class.getSimpleName() + "(type=String.class, name=\"name\")\n" + " },\n" + " hasMany = {\n" + "  @" + Relation.class.getSimpleName() + "(type=Car.class, name=\"cars\", opposite=\"drivers\")\n" + " }\n" + ")";
        driver = new TestModel("Driver", a2);
        addModel(car);
        addModel(driver);
        createModels();
        migrate();
        Persistable d = driver.newInstance(server);
        Persistable c1 = car.newInstance(server);
        get(d, "cars", Set.class).add(c1);
        set(c1, "name", "Car1");
        d.save();
        int id = d.getId();
        assertTrue(id > 0);
        server.clearReferences();
        d = server.persistor().find(driver.getModelClass(), id);
        assertNotNull(d);
        Set cars = get(d, "cars", Set.class);
        assertEquals(1, cars.size());
        assertEquals("Car1", get(cars.iterator().next(), "name"));
        router.addRoute(driver.getModelClass());
        router.addRoute(car.getModelClass());
        d = client.persistor().find(driver.getModelClass(), id);
        assertNotNull(d);
        cars = get(d, "cars", Set.class);
        assertEquals(1, cars.size());
        assertEquals("Car1", get(cars.iterator().next(), "name"));
        Persistable c2 = car.newInstance(client);
        set(c2, "name", "Car2");
        cars.add(c2);
        client.clearReferences();
        server.clearReferences();
        d = client.persistor().find(driver.getModelClass(), id);
        assertNotNull(d);
        cars = get(d, "cars", Set.class);
        assertEquals(2, cars.size());
        Iterator<?> iter = cars.iterator();
        assertEquals("Car1", get(iter.next(), "name"));
        assertEquals("Car2", get(iter.next(), "name"));
    }
}
