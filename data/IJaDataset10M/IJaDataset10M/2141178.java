package dk.pervasive.jcaf.examples;

import dk.pervasive.jcaf.ContextEvent;
import dk.pervasive.jcaf.item.Location;
import dk.pervasive.jcaf.relationship.Located;
import dk.pervasive.jcaf.rest.client.RestContextClient;
import dk.pervasive.jcaf.rest.client.listener.EntityListenerInfo;
import dk.pervasive.jcaf.rest.client.listener.RestEntityListener;
import dk.pervasive.jcaf.rest.entity.RestPerson;
import dk.pervasive.jcaf.rest.entity.RestPlace;

public class TestClientContextService_WithAndroidEntities {

    public static void main(String[] args) {
        RestContextClient client = new RestContextClient(10000);
        RestPerson myPerson = new RestPerson("vv", "Custom");
        client.getContextService().addEntity(myPerson);
        EntityListenerInfo info = new EntityListenerInfo(RestPerson.class);
        client.getContextService().registerEntityListener(new RestEntityListener(info) {

            @Override
            public void contextChanged(ContextEvent event) {
                System.out.println("Entity " + event.getEntity().toString() + "\n changed by \n" + event.getRelationship() + "\n" + event.getItem());
            }
        });
        RestPerson v1 = (RestPerson) client.getContextService().getEntity("vv");
        System.out.println("[getEntity] Person: " + v1);
        client.getContextService().addEntity(new RestPerson("myGuy", "Johnny"));
        client.getContextService().addEntity(new RestPlace("myPlace", "Planetarium"));
        client.getContextService().addContextItem("myGuy", new Located("star gazing"), new Location("id2", "myPlace"));
    }
}
