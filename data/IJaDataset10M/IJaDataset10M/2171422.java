package org.nicocube.airain.domain.client.character;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.nicocube.airain.domain.client.character.Possession.Type;
import org.nicocube.airain.domain.client.gamedate.BirthDate;
import org.nicocube.airain.domain.client.gamedate.GameDate;
import org.nicocube.airain.domain.client.utils.DomainException;
import org.nicocube.airain.domain.client.utils.Publisher;
import org.nicocube.airain.domain.client.utils.SequenceHolder;
import org.nicocube.airain.domain.client.utils.Subscriber;

/**
 * Observable version of a GameCharacter. Work as a decorator with delegate methods.
 * @author nicolas
 *
 */
public class ObservableGameCharacter extends GameCharacter implements Publisher {

    private static final long serialVersionUID = 1L;

    private final GameCharacter gc;

    private List<Subscriber> subscribers;

    public ObservableGameCharacter(GameCharacter gc) {
        this.gc = gc;
        subscribers = new LinkedList<Subscriber>();
    }

    public void addPossession(Possession possession) {
        gc.addPossession(possession);
        publish();
    }

    public List<Object> dependencies() {
        return gc.dependencies();
    }

    public boolean equals(Object obj) {
        return gc.equals(obj);
    }

    public Level getAttributeLevel(AttributeIdentifier attribute) {
        return gc.getAttributeLevel(attribute);
    }

    public BirthDate getBirthdate() {
        return gc.getBirthdate();
    }

    public GameDate getLastEatingHour() {
        return gc.getLastEatingHour();
    }

    public GameDate getLastSleepingHour() {
        return gc.getLastSleepingHour();
    }

    public int getLastSleepLength() {
        return gc.getLastSleepLength();
    }

    public GameDate getLivedate() {
        return gc.getLivedate();
    }

    public String getName() {
        return gc.getName();
    }

    public Long getOrder() {
        return gc.getOrder();
    }

    public Queue<Possession> getPossessionByType(Type type) {
        return gc.getPossessionByType(type);
    }

    public Expertise getSkillExpertise(SkillIdentifier skill) {
        return gc.getSkillExpertise(skill);
    }

    public int hashCode() {
        return gc.hashCode();
    }

    public boolean isSaved() {
        return gc.isSaved();
    }

    public void setLastEatingHour(GameDate lastEatingHour) {
        gc.setLastEatingHour(lastEatingHour);
        publish();
    }

    public void setLastSleepingHour(GameDate lastSleepingHour) {
        gc.setLastSleepingHour(lastSleepingHour);
        publish();
    }

    public void setLastSleepLength(int lastSleepLength) {
        gc.setLastSleepLength(lastSleepLength);
        publish();
    }

    public void setLivedate(GameDate livedate) {
        gc.setLivedate(livedate);
        publish();
    }

    public void setOrder(SequenceHolder sequenceHolder) throws DomainException {
        gc.setOrder(sequenceHolder);
        publish();
    }

    public void setSkillExpertise(SkillIdentifier ident, Expertise value) {
        gc.setSkillExpertise(ident, value);
        publish();
    }

    public void synchronise(GameCharacter result) {
        gc.synchronise(result);
        publish();
    }

    public String toString() {
        return gc.toString();
    }

    public void publish() {
        for (Subscriber subscriber : subscribers) subscriber.update(this);
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
}
