package net.sf.laja.example.car.behaviour;

import net.sf.laja.example.car.state.car.CarBehaviourFactory;
import net.sf.laja.example.car.state.car.CarState;
import net.sf.laja.example.car.state.car.CarStateBuilder;
import net.sf.laja.example.car.state.car.CarStateListBuilder;
import net.sf.laja.example.car.state.car.CarSubstate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.sf.laja.example.car.state.car.*;

public class Car extends net.sf.laja.example.car.state.car.CarBehaviour {

    private final VehicleSize size;

    public Car(CarSubstate state) {
        super(state);
        size = new VehicleSize(state);
    }

    public Car asCar() {
        return new Car(state);
    }

    public boolean isBig() {
        return size.isBig();
    }

    public static Creator.Name_ lengthInCentimeters(int lengthInCentimeters) {
        return new Creator().new LengthInCentimeters_().lengthInCentimeters(lengthInCentimeters);
    }

    public static Builder build() {
        return new Builder();
    }

    public static ListCreator createList(Creator.Encapsulator... encapsulators) {
        return new ListCreator(encapsulators);
    }

    public static class Builder {

        public final CarStateBuilder builder;

        public Builder() {
            builder = CarState.build();
        }

        public Builder(CarSubstate state) {
            builder = CarState.build(state);
        }

        public Builder withLengthInCentimeters(int lengthInCentimeters) {
            builder.withLengthInCentimeters(lengthInCentimeters);
            return this;
        }

        public Builder withName(String name) {
            builder.withName(name);
            return this;
        }

        public Builder withColor(CarColor color) {
            builder.withColor(color);
            return this;
        }

        public Builder withColor(String color) {
            builder.withColor(color);
            return this;
        }

        public Builder withOwner(net.sf.laja.example.car.behaviour.Owner.Builder owner) {
            builder.withOwner(owner.builder);
            return this;
        }

        public boolean isValid() {
            return builder.isValid();
        }

        public Car asCar() {
            return (Car) builder.as(new CarFactory_());
        }
    }

    public static class Creator {

        private final CarStateBuilder builder = CarState.build();

        public class LengthInCentimeters_ {

            public Name_ lengthInCentimeters(int lengthInCentimeters) {
                builder.withLengthInCentimeters(lengthInCentimeters);
                return new Name_();
            }
        }

        public class Name_ {

            public Color_ name(String name) {
                builder.withName(name);
                return new Color_();
            }
        }

        public class Color_ {

            public Owner_ color(CarColor color) {
                builder.withColor(color);
                return new Owner_();
            }

            public Owner_ color(String color) {
                builder.withColor(color);
                return new Owner_();
            }
        }

        public class Owner_ {

            public Encapsulator owner(net.sf.laja.example.car.behaviour.Owner.Creator.Encapsulator owner) {
                builder.withOwner(owner.builder);
                return new Encapsulator(builder);
            }
        }

        public static class Encapsulator {

            public final CarStateBuilder builder;

            public Encapsulator(CarStateBuilder builder) {
                this.builder = builder;
            }

            public Car asCar() {
                return (Car) builder.as(new CarFactory_());
            }

            public boolean isValid() {
                return builder.isValid();
            }
        }
    }

    public static class CarFactory_ implements CarBehaviourFactory {

        public Object create(CarSubstate state, Object... args) {
            Object result = create_(state, args);
            if (!state.isValid()) {
                throw new IllegalStateException("Illegal state, could not create behaviour class 'Car'");
            }
            return result;
        }

        private Object create_(CarSubstate state, Object... args) {
            return new Car(state);
        }
    }

    public static class ListCreator implements Iterable<Creator.Encapsulator> {

        public CarStateListBuilder builders = new CarStateListBuilder();

        private List<Creator.Encapsulator> list = new ArrayList<Creator.Encapsulator>();

        public void add(Creator.Encapsulator encapsulator) {
            list.add(encapsulator);
            builders.add(encapsulator.builder);
        }

        public ListCreator(Creator.Encapsulator... encapsulators) {
            list.addAll(Arrays.asList(encapsulators));
        }

        public Iterator<Creator.Encapsulator> iterator() {
            return list.iterator();
        }

        public CarList asCarList() {
            List<Car> result = new ArrayList<Car>();
            for (Creator.Encapsulator encapsulator : list) {
                result.add(encapsulator.asCar());
            }
            return new CarList(result);
        }

        public boolean isValid() {
            for (Creator.Encapsulator encapsulator : list) {
                if (!encapsulator.isValid()) {
                    return false;
                }
            }
            return true;
        }
    }
}
