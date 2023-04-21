package edu.opexcavator.model.postype;

import java.util.Map;

/**
 * @author Jesica N. Fera
 *
 */
public class Preposition extends POSType {

    protected static Map<String, Type> typeTags;

    protected static Map<String, Form> formTags;

    protected static Map<String, Gender> genderTags;

    protected static Map<String, GrammaticNumber> numberTags;

    private Type type;

    private Form form;

    private Gender gender;

    private GrammaticNumber number;

    public Preposition(Type type, Form form, Gender gender, GrammaticNumber number) {
        this.type = type;
        this.form = form;
        this.gender = gender;
        this.number = number;
    }

    /**
	 * Creates a preposition without any additional information.
	 **/
    public Preposition() {
    }

    public static Type buildType(String typeTag) {
        return typeTags != null ? typeTags.get(typeTag.toLowerCase()) : null;
    }

    public static Gender buildGender(String genderTag) {
        return genderTags != null ? genderTags.get(genderTag.toLowerCase()) : null;
    }

    public static GrammaticNumber buildNumber(String numberTag) {
        return numberTags != null ? numberTags.get(numberTag.toLowerCase()) : null;
    }

    public static Form buildForm(String formTag) {
        return formTags != null ? formTags.get(formTag.toLowerCase()) : null;
    }

    public Type getType() {
        return type;
    }

    public Form getForm() {
        return form;
    }

    public Gender getGender() {
        return gender;
    }

    public GrammaticNumber getNumber() {
        return number;
    }

    public enum Type {

        Preposition, None
    }

    public enum Form {

        Simple, Contracted, None
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((form == null) ? 0 : form.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }
}
