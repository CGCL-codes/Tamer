package fido.linguistic.components;

/**
 * Represents a pronoun, which tells the system to look for a noun
 * in the Discourse to replace.<P>
 * 
 * The following table shows the pronouns for English:<P>
 *
 * <TABLE BORDER=1>
 * 	<TH>Group</TH>
 * 	<TH>Person</TH>
 * 	<TH>Gender</TH>
 * 	<TH>Subjective</TH>
 * 	<TH>Objective</TH>
 * 	<TH>Possessive</TH>
 * 	<TH>Possessive<BR>Adjective</TH>
 * 	<TR>
 * 		<TD>false</TD>
 * 		<TD>First</TD>
 * 		<TD>&nbsp;</TD> 	
 * 		<TD><I>I</I></TD>
 * 		<TD><I>me</I></TD>
 * 		<TD><I>mine</I></TD>
 * 		<TD><I>my</I></TD>
 * 	</TR>
 * 	<TR>
 * 		<TD>&nbsp;</TD>
 * 		<TD>Second</TD>
 * 		<TD>&nbsp;</TD>
 * 		<TD><I>you</I></TD>
 * 		<TD><I>you</I></TD>
 * 		<TD><I>yours</I></TD>
 * 		<TD><I>your</I></TD>
 * 	</TR>
 * 	<TR>
 * 		<TD>&nbsp;</TD>
 * 		<TD>Third</TD>
 * 		<TD>masculine</TD>
 * 		<TD><I>he</I></TD>
 * 		<TD><I>him</I></TD>
 * 		<TD><I>his</I></TD>
 * 		<TD><I>his</I></TD>
 * 	</TR>
 * 	<TR>
 * 		<TD>&nbsp;</TD>
 * 		<TD>&nbsp;</TD>
 * 		<TD>feminine</TD>
 * 		<TD><I>she</I></TD>
 * 		<TD><I>her</I></TD>
 * 		<TD><I>hers</I></TD>
 * 		<TD><I>her</I></TD>
 * 	</TR>
 * 	<TR>
 * 		<TD>&nbsp;</TD>
 * 		<TD>&nbsp;</TD>
 * 		<TD>neuter</TD>
 * 		<TD><I>it</I></TD>
 * 		<TD><I>it</I></TD>
 * 		<TD>&nbsp;</TD>
 * 		<TD><I>its</I></TD>
 * 	</TR>
 * 	<TR>
 * 		<TD>true</TD>
 * 		<TD>First</TD>
 * 		<TD>&nbsp;</TD>
 * 		<TD><I>we</I></TD>
 * 		<TD><I>us</I></TD>
 * 		<TD><I>ours</I></TD>
 * 		<TD><I>our</I></TD>
 * 	</TR>
 * 	<TR>
 * 		<TD>&nbsp;</TD>
 * 		<TD>Second</TD>
 * 		<TD>&nbsp;</TD>
 * 		<TD><I>you</I></TD>
 * 		<TD><I>you</I></TD>
 * 		<TD><I>yours</I></TD>
 * 		<TD><I>your</I></TD>
 * 	</TR>
 * 	<TR>
 * 		<TD>&nbsp;</TD>
 * 		<TD>Third</TD>
 * 		<TD>&nbsp;</TD>
 * 		<TD><I>they</I></TD>
 * 		<TD><I>them</I></TD>
 * 		<TD><I>theirs</I></TD>
 * 		<TD><I>their</I></TD>
 * 	</TR>
 * </TABLE>
 * 
 * @see fido.linguistic.Discourse
 * @see fido.linguistic.PronounResolver
 */
public class Pronoun implements Noun {

    /** Constant for setPerson() and getPerson() */
    public static final int FIRST = 1;

    /** Constant for setPerson() and getPerson() */
    public static final int SECOND = 2;

    /** Constant for setPerson() and getPerson() */
    public static final int THIRD = 3;

    /** Constants for setGender() and getGender() */
    public static final int MALE = 1;

    /** Constants for setGender() and getGender() */
    public static final int FEMALE = 2;

    /** Constants for setGender() and getGender() */
    public static final int NEUTER = 3;

    private int person;

    private int gender;

    private boolean group;

    /**
	 * Creates a new Pronoun object.  Person and Gender parameters should
	 * use the constant values defined within this class.
	 * 
	 * @param person Person of the pronoun.
	 * @param gender Gender of the pronoun.
	 * @param group Identifies the pronoun as representing a group.
	 */
    public Pronoun(int person, int gender, boolean group) {
        this.person = person;
        this.gender = gender;
        this.group = group;
    }

    /**
	 * Returns the person for this pronoun.  Person can hold the following
	 * constant values:
	 * <UL>
	 * <LI> FIRST - resolves to the speaker
	 * <LI> SECOND - resolves to the listener
	 * <LI> THIRD - resolves to some third party
	 * </UL>
	 * Constants for Person type are defined in PronounTable.
	 *
	 * @return Constant int value for the person.
	 *
	 * @see fido.db.PronounTable#FIRST
	 * @see fido.db.PronounTable#SECOND
	 * @see fido.db.PronounTable#THIRD
	 */
    public int getPerson() {
        return person;
    }

    /**
	 * Returns the gender for this pronoun.  Gender can hold the following
	 * constant values:
	 * <UL>
	 * <LI> MALE
	 * <LI> FEMALE
	 * <LI> NEUTER
	 * </UL>
	 * Constants for Gender type are defined in PronounTable.
	 *
	 * @return Constant int value for the gender.
	 *
	 * @see fido.db.PronounTable#MALE
	 * @see fido.db.PronounTable#FEMALE
	 * @see fido.db.PronounTable#NEUTER
	 */
    public int getGender() {
        return gender;
    }

    /**
	 * Returns if the pronoun represents a group.
	 * 
	 * @return True if the pronoun represents a group.  False otherwise.
	 */
    public boolean isGroup() {
        return group;
    }
}
