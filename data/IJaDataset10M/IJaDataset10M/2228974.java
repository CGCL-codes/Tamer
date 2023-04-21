package org.avis.client.examples;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.avis.client.Elvin;
import org.avis.client.GeneralNotificationEvent;
import org.avis.client.GeneralNotificationListener;
import org.avis.security.Key;
import org.avis.security.Keys;
import static org.avis.client.SecureMode.REQUIRE_SECURE_DELIVERY;
import static org.avis.security.KeyScheme.SHA1_CONSUMER;

public class SecureReceiver {

    public static void main(String[] args) throws Exception {
        Elvin elvin = new Elvin(System.getProperty("elvin", "elvin://localhost"));
        elvin.closeOnExit();
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the password for receiving: ");
        String password = stdin.readLine();
        Key privateKey = new Key(password);
        Keys keys = new Keys();
        keys.add(SHA1_CONSUMER, privateKey);
        elvin.subscribe("From == 'secure-sender' && string (Message)", keys, REQUIRE_SECURE_DELIVERY);
        elvin.addNotificationListener(new GeneralNotificationListener() {

            public void notificationReceived(GeneralNotificationEvent e) {
                System.out.println("Received message: " + e.notification.get("Message"));
            }
        });
        System.out.println("Listening for messages...");
    }
}
