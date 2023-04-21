package org.eclipse.ufacekit.core.examples.databinding.instance.beans;

import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.ufacekit.core.databinding.instance.IInstanceObservedChanged;

public class PersonTest2 {

    private static Person person1 = null;

    private static Person person2 = null;

    public static void main(String[] args) {
        person1 = new Person();
        person2 = null;
        IObserving observing1 = new IObserving() {

            public Object getObserved() {
                return person1;
            }
        };
        IObserving observing2 = new IObserving() {

            public Object getObserved() {
                return person2;
            }
        };
        IInstanceObservedChanged instanceChanged = new IInstanceObservedChanged() {

            public void instanceChanged(Object oldInstance, Object newInstance) {
                System.out.println("Instance changed : oldInstance=" + oldInstance + ", newInstance=" + newInstance);
            }
        };
        PersonContainer container = new PersonContainer();
        container.addObserving(observing1, instanceChanged);
        container.addObserving(observing2, instanceChanged);
        System.out.println("--- Test No change ---");
        container.observeInstances();
        System.out.println("--- Test Change person1 instance ---");
        person1 = new Person();
        container.observeInstances();
        System.out.println("--- Test Change person1 + person2 instance ---");
        person1 = null;
        person2 = new Person();
        container.observeInstances();
        container.dispose();
    }
}
