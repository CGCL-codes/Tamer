package test.hibernate21;

/**
 * @hibernate.joined-subclass
 *  table="REPTILES"
 * @hibernate.joined-subclass-key
 *  column="ANIMAL_ID"
 */
public abstract class Reptile extends Animal {
}
