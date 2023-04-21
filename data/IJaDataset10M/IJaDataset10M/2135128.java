package com.zorgly.nabaztag;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import com.zorgly.nabaztag.sdk.Choreography;
import com.zorgly.nabaztag.sdk.LedAction;
import com.zorgly.nabaztag.sdk.MotorAction;
import com.zorgly.nabaztag.sdk.Rabbit;
import com.zorgly.nabaztag.sdk.RabbitPlayer;
import com.zorgly.nabaztag.sdk.Rabbit.Led;
import com.zorgly.nabaztag.sdk.Rabbit.Nabcast;
import com.zorgly.nabaztag.sdk.Rabbit.Voice;
import com.zorgly.nabaztag.sdk.Rabbit.Voices;
import com.zorgly.nabaztag.sdk.Rabbit.Ear.Motor;

/**
 * Main class to simply test the Nabaztag SDK.
 * 
 * @author Vassyly Lygeros
 */
public class NabaztagApp {

    public static void main(String[] args) {
        String name = args[0];
        String serialNumber = args[1];
        String token = args[2];
        Rabbit rabbit = new Rabbit(name, serialNumber, token);
        rabbit.getLeftEar().setPosition(16);
        rabbit.getRightEar().setPosition(16);
        Voice voice = rabbit.getVoice();
        voice.setVoice(Voices.French.CAROLINE22K);
        rabbit.setVoice(voice);
        RabbitPlayer player = new RabbitPlayer();
        System.out.println("Preview : " + player.getTtsOrMusicPreview(rabbit));
        Collection friends = player.getFriends(rabbit);
        for (Iterator iterFriend = friends.iterator(); iterFriend.hasNext(); ) {
            String friendName = (String) iterFriend.next();
            System.out.println("Friend : " + friendName);
        }
        Collection messages = player.getMessages(rabbit);
        for (Iterator iterMessage = messages.iterator(); iterMessage.hasNext(); ) {
            Map map = (Map) iterMessage.next();
            System.out.println("Message : from=" + map.get("from") + ", title=" + map.get("title") + ", date=" + map.get("date") + ", url=" + map.get("url"));
        }
        System.out.println("Timezone : " + player.getTimezone(rabbit));
        System.out.println("Signature : " + player.getSignature(rabbit));
        Collection blackListed = player.getBlackListed(rabbit);
        for (Iterator iterBlackListed = blackListed.iterator(); iterBlackListed.hasNext(); ) {
            String blackListedName = (String) iterBlackListed.next();
            System.out.println("BlackListed : " + blackListedName);
        }
        boolean isSleeping = player.isSleeping(rabbit);
        if (isSleeping) System.out.println("Is sleeping : YES"); else System.out.println("Is sleeping : NO");
        System.out.println("Version : " + player.getVersion(rabbit));
        Collection supportedVoices = player.getSupportedVoices(rabbit);
        for (Iterator iterSupportedVoice = supportedVoices.iterator(); iterSupportedVoice.hasNext(); ) {
            Map map = (Map) iterSupportedVoice.next();
            System.out.println("SupportedVoice : lang=" + map.get("lang") + ", command=" + map.get("command"));
        }
        System.out.println("Name : " + player.getName(rabbit));
        Collection languages = player.getLanguages(rabbit);
        for (Iterator iterLanguage = languages.iterator(); iterLanguage.hasNext(); ) {
            String language = (String) iterLanguage.next();
            System.out.println("Language : lang=" + language);
        }
        System.out.println("Message preview : " + player.getMessagePreview(rabbit));
        player.sleep(rabbit);
        player.wakeUp(rabbit);
        player.setEars(rabbit);
        player.setVoice(rabbit);
        player.playText(rabbit, "Bonjour, je m'appelle ZLagos SDK");
        player.playMessage(rabbit, "3376409");
        MotorAction action1 = new MotorAction(0, rabbit.getLeftEar().getMotor());
        action1.setAngle(180);
        action1.setRotation(Motor.FRONT_ROTATION);
        MotorAction action2 = new MotorAction(1, rabbit.getRightEar().getMotor());
        action2.setAngle(0);
        action2.setRotation(Motor.BACK_ROTATION);
        LedAction action3 = new LedAction(2, rabbit.getLed(Led.TOP));
        action3.setColor(127, 255, 0);
        LedAction action4 = new LedAction(2, rabbit.getLed(Led.BOTTOM));
        action4.setColor(127, 255, 0);
        LedAction action5 = new LedAction(2, rabbit.getLed(Led.LEFT));
        action5.setColor(0, 255, 127);
        LedAction action6 = new LedAction(2, rabbit.getLed(Led.RIGHT));
        action6.setColor(0, 255, 127);
        LedAction action7 = new LedAction(2, rabbit.getLed(Led.CENTER));
        action7.setColor(127, 127, 127);
        Choreography choreography = new Choreography(10);
        choreography.setTitle("Zorgly choreography");
        choreography.addAction(action1);
        choreography.addAction(action2);
        choreography.addAction(action3);
        choreography.addAction(action4);
        choreography.addAction(action5);
        choreography.addAction(action6);
        choreography.addAction(action7);
        player.play(rabbit, choreography);
        Nabcast nabcast = rabbit.getNabcast(52);
        nabcast.setTitle("My nabcast");
        player.publishText(nabcast, "Bonjour, je m'appelle ZLagos SDK");
        player.publishMessage(nabcast, "3376409");
        player.publish(nabcast, action1);
        player.publish(nabcast, choreography);
    }
}
