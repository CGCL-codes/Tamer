package guidoengine;

import guidoengine.*;
import java.awt.Graphics;
import java.awt.Canvas;

/**
	The GUIDO Factory provides a set of methods to dynamically create a GUIDO
    abstract representation.
    
    The GUIDO Factory is a state machine that operates on implicit current elements:
    for example, once you open a voice ({@code OpenVoice()}), it becomes the current
    voice and all subsequent created events are implicitly added to this current voice. 
    The elements of the factory state are:
	<ul>
    <li> the current score: modified by {@code OpenMusic()} and {@code CloseMusic()} </li>
    <li> the current voice: modified by {@code OpenVoice()} and {@code CloseVoice()} </li>
    <li> the current chord: modified by {@code OpenChord()} and {@code CloseChord()} </li>
    <li> the current event: modified by {@code OpenEvent()} and {@code CloseEvent()} </li>
    <li> the current tag: modified by {@code OpenTag()} and {@code CloseTag()} </li>
	</ul>
*/
public class guidofactory {

    public final long fFactoryHandler;

    public guidofactory() {
        fFactoryHandler = 0;
    }

    /**  Opens the Guido Factory.
	
		Must be called before any other call to the Guido Factory API.
		@return an integer that is an error code if not null.
	*/
    public final native synchronized int Open();

    /**  Closes the Guido Factory.

		Must be called to release the factory associated resources.
	*/
    public final native synchronized void Close();

    /**  Creates and opens a new music score. 

		The function modifies the factory state: the new score becomes
		the current factory score. 
		It fails if a music score is already opened.
		A music score has to be closed using {@code CloseMusic()}
		@return an integer that is an error code if not null.
		@see guidofactory#CloseMusic
	*/
    public final native synchronized int OpenMusic();

    /**  Closes the current music score.
		
		The function modifies the factory state if a music score is currently opened: 
		the current factory score is set to null.
		It fails if no music score is opened.
		You must not have pending events nor pending voice at this point.
		
		The logicical music layout (conversion from abstract to abstract representation)
		is part of the function operations. 

		@return a GUIDO handler to the new AR structure, or 0. This handler may be used to build
		a new guidoscore. 
		@see guidofactory#OpenMusic
	*/
    public final native synchronized long CloseMusic();

    /**  Creates and opens a new voice.

		The function modifies the factory state: 
		the new voice becomes the current factory voice. 
		It fails if a voice is already opened.
		A voice has to be closed using {@code CloseVoice()}
		Voices are similar to sequence is GMN.

		@return an error code
		@see guidofactory#CloseVoice
	*/
    public final native synchronized int OpenVoice();

    /**  Closes the current voice.
		
		The function modifies the factory state if a voice is currently opened: 
		the current factory voice is set to null.
		It fails if no voice is opened.
		You must not have pending events at this point.
		The voice is first converted to its normal form and next added to the current score.

		@return an error code
		@see guidofactory#OpenVoice
	*/
    public final native synchronized int CloseVoice();

    /**  Creates and open a new chord.

		The function modifies the factory state: the new chord becomes the current factory chord. 
		It fails if a chord is already opened.
		A chord has to be closed using {@code CloseChord()}

		@return an error code
		@see guidofactory#CloseChord
	*/
    public final native synchronized int OpenChord();

    /**  Closes the current chord.
		
		The function modifies the factory state if a chord is currently opened: 
		the current factory chord is set to null.
		It fails if no chord is opened.
		The chord is added to the current voice.
		
		@return an error code
		@see guidofactory#OpenChord
	*/
    public final native synchronized int CloseChord();

    /**  Begins a new chord note commata.

		Called to tell the factory that a new chord-voice
		is beginning. This is important for the ranges that need to
		be added (dispdur and shareStem)

		@return an error code
	*/
    public final native synchronized int InsertCommata();

    /**  Creates and opens a new event (note or rest).

		The function modifies the factory state: the new event becomes the current factory event. 
		It fails if an event is already opened.
		An event has to be closed using {@code CloseEvent()}
		
		@param eventName a note, rest or empty name confroming to the GMN format
		@return an error code
		@see guidofactory#CloseEvent
	*/
    public final native synchronized int OpenEvent(String eventName);

    /**  Closes the current event.

		The function modifies the factory state if an event is currently opened: 
		the current factory event is set to null.
		It fails if no event is opened.
		The event is added to the current voice.

		@return an error code
		@see guidofactory#OpenEvent
	*/
    public final native synchronized int CloseEvent();

    /**  Adds a sharp to the current event. 

		The current event must be a note.
		@return an error code
	*/
    public final native synchronized int AddSharp();

    /**  Add a flat to the current event. 

		The current event must be a note.
		@return an error code.
	*/
    public final native synchronized int AddFlat();

    /** Sets the number of dots of the current event.
		@param dots the number of dots to be carried by the current event.
		@return an error code.
	*/
    public final native synchronized int SetEventDots(int dots);

    /**  Sets the accidentals of the current event.
		@param accident  positive values are used for sharp and negative values for flats 
		@return an error code.
	*/
    public final native synchronized int SetEventAccidentals(int accident);

    /**  Sets the octave of the current event.

		The current  event must be a note.
		The octave number becomes the current octave i.e. next notes will carry this
		octave number until otherwise specified.
		
		@param octave is an integer value indicating the octave of the note where 
			a1 is A 440Hz. All octaves start with the pitch class {@code c}.
		@return an error code.
	*/
    public final native synchronized int SetOctave(int octave);

    /**  Sets the duration of the current event.

		Durations are expressed as fractional value of a whole note: 
		e.g. a quarter note duration is 1/4.
		The duration becomes the current duration i.e.  next notes will carry this
		duration until otherwise specified.

		@param numerator the rational duration numerator
		@param denominator the rational duration denominator
		@return an error code.
	*/
    public final native synchronized int SetDuration(int numerator, int denominator);

    /** Add a tag to the current voice.

		@param tagName the tag name
		@param tagID is the number that the parser generates for advanced GUIDO ?????
		@return an error code.
	*/
    public final native synchronized int OpenTag(String tagName, long tagID);

    /** Indicates that the current tag is a range tag.
	
			@return an error code.
	*/
    public final native synchronized int IsRangeTag();

    /** Indicates the end of a range tag.

		The function is applied to the current tag.
		It must be called when the end of a tag's range has been reached. 
		If the tag has no range, it must be called directly after {@code CloseTag()}.
		
		With the following examples:
		<ul>
		<li> {@code staff<1> c d}  : call {@code EndTag()} after {@code CloseTag()} and 
					before creating the {@code c} note </li>
		<li> {@code slur(c d e) f} : call {@code EndTag()} before creating the {@code f} note </li>
		</ul>

		@return an error code.
	*/
    public final native synchronized int EndTag();

    /**  Closes the current tag.

		The function is applied to the current tag.
		Must be called after adding parameter and before the range.
		With the following examples:
		<ul>
		<li> {@code tag<1,2,3>(c d e )} : call {@code CloseTag()}, next {@code IsRangeTag()}
			creating the {@code c d e} notes and call {@code EndTag()}
		<li> {@code tag<1,2> c d}       : call {@code CloseTag()} before creating the {@code c} note
		</ul>

		@return an error code.
	*/
    public final native synchronized int CloseTag();

    /**  Adds a new string parameter to the current tag.

		@param val the string parameter value
		@return an error code.
	*/
    public final native synchronized int AddTagParameterString(String val);

    /**   Adds a new integer parameter to the current tag.

		@param val the parameter value
		@return an error code.
	*/
    public final native synchronized int AddTagParameterInt(int val);

    /**  Adds a new floating-point parameter to the current tag.

		@param val the parameter value
		@return an error code.
	*/
    public final native synchronized int AddTagParameterFloat(double val);

    /**  Defines the name (when applicable) of the last added tag-parameter

		@param name the tag parameter name
		@return an error code.
	*/
    public final native synchronized int SetParameterName(String name);

    /**  Defines the unit of the last added tag-parameter

		@param unit a string defining the unit. The following units are supported:
		<ul>
		<li> {@code m} - meter </li>
		<li> {@code cm} - centimeter </li>
		<li> {@code mm} - millimeter </li>
		<li> {@code in} - inch </li>
		<li> {@code pt} - point (= 1/72.27 inch) </li>
		<li> {@code pc} - pica (= 12pt) </li>
		<li> {@code hs} - halfspace (half of the space between  two lines of the current staff) </li>
		<li> {@code rl} - relative measure in percent  (used for positioning on score page) </li>
		</ul>
		@return an error code.
	*/
    public final native synchronized int SetParameterUnit(String unit);

    /** Internal jni initialization method.
		Automatically called at package init.
	*/
    protected static native void Init();
}
