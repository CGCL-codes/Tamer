package com.flagstone.transform;

/** 
The FSAction class is used to represent stack-based actions, defined by simple byte-codes, that 
are executed by the Flash Player.
 
<p>The operations supported by the FSAction class are:</p>

<h1 class="datasheet">Stack Manipulation</h1>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>Pop</td>
    <td>Pop value from the top of the stack.</td>
    <td nowrap>(valueA -- )</td>
    <td nowrap>(4 -- )</td></tr>
<tr valign="top"><td>Duplicate</td>
    <td>Duplicate the value at the top of the stack.</td>
    <td nowrap>(valueA -- valueA valueA)</td>
    <td nowrap>(4 -- 4 4)</td></tr>
<tr valign="top"><td>Swap</td>
    <td>Swap the top two values on the stack.</td>
    <td nowrap>(valueA valueB -- valueB valueA)</td>
    <td nowrap>(4 3 -- 3 4)</td></tr>
</table>

<p>FSPush is used to push literals onto the Stack. See also FSRegisterCopy which 
copies the value on top of the Stack to one of the Flash Player's internal 
registers.</p>
    
<h1 class="datasheet">Arithmetic</h1>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>Add</td>
    <td>Arithmetic Add: A + B</td>
    <td nowrap>(numA numB -- num)</td>
    <td nowrap>(4 3 -- 7)</td></tr>
<tr valign="top"><td>Subtract</td>
    <td>Arithmetic Subtract: A - B</td>
    <td nowrap>(numA numB -- num)</td>
    <td nowrap>(4 3 -- 1)</td></tr>
<tr valign="top"><td>Multiply</td>
    <td>Arithmetic Multiply: A * B</td>
    <td nowrap>(numA numB -- num)</td>
    <td nowrap>(4 3 -- 12)</td></tr>
<tr valign="top"><td>Divide</td>
    <td>Arithmetic Divide: A / B</td>
    <td nowrap>(numA numB -- num)</td>
    <td nowrap>(4 3 -- 1.333)</td></tr>
<tr valign="top"><td>Modulo</td>
    <td>Arithmetic Modulo: A % B</td>
    <td nowrap>(numA numB -- num)</td>
    <td nowrap>(4 3 -- 1)</td></tr>
<tr valign="top"><td>Increment</td>
    <td>Add 1 to the value on the stack.</td>
    <td nowrap>(num -- num)</td>
    <td nowrap>(3 -- 4)</td></tr>
<tr valign="top"><td>Decrement</td>
    <td>Subtracted 1 from the value on the stack.</td>
    <td nowrap>(num -- num)</td>
    <td nowrap>(4 -- 3)</td></tr>
</table>

<p>Arithmetic add is supported by two actions. IntegerAdd was introduced in Flash 4. 
It was replaced in Flash 5 by the more flexible Add action which is able to add 
any two numbers and also concatenate strings. If a string and a number are added 
then the number is converted to its string representation before concatenation.</p>

<h1 class="datasheet">Comparison</h1>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>Less</td>
    <td>LessThan: A &lt; B</td>
    <td nowrap>(numA numB -- boolean)</td>
    <td nowrap>(10 9 -- 0 )</tr>
<tr valign="top"><td>StringLess</td>
    <td>String compare: stringA &lt; stringB</td>
    <td nowrap>(stringA stringB -- boolean)</td>
    <td nowrap>("abc" "ab" -- 0)</td></tr>
<tr valign="top"><td>Equals</td>
    <td>Equals: A == B</td>
    <td nowrap>(numA numB -- boolean)</td>
    <td nowrap>(23 23 -- 1 )</tr>
<tr valign="top"><td>StringEquals</td>
    <td>String compare: stringA == stringB</td>
    <td nowrap>(stringA stringB -- boolean)</td>
    <td nowrap>("abc" "abc" -- 1)</td></tr>
<tr valign="top"><td>StrictEquals</td>
    <td>Equals: A === B, are the types as well as the values equal.</td>
    <td nowrap>(valueA valueB -- boolean)</td>
    <td nowrap>("23" 23 -- 0 )</tr>
<tr valign="top"><td>Greater</td>
    <td>Greater Than: A > B</td>
    <td nowrap>(numA numB -- boolean)</td>
    <td nowrap>(10 9 -- 0 )</tr>
<tr valign="top"><td>StringGreater</td>
    <td>String compare: stringA > stringB</td>
    <td nowrap>(stringA stringB -- boolean)</td>
    <td nowrap>("abc" "ab" -- 0)</td></tr>
</table>

<p>The less than comparison is supported by IntegerLess introduced in Flash 4 
and Less introduced in Flash 5. The Less action is more flexible allowing comparison 
between any combination of two numbers and strings. In Flash 4 comparisons were 
only supported on values of the same type using either IntegerLess or StringLess.</p>

<p>The equals comparison is supported by IntegerEquals introduced in Flash 4 and 
Equals introduced in Flash 5. The Equals action is more flexible allowing 
comparison between any combination of two numbers and strings. In Flash 4 
comparisons were only supported on values of the same type using either
IntegerEquals or StringEquals.</p>
    
<h1 class="datasheet">Logical</h1>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>And</td>
    <td>Logical And: A &amp;&amp; B</td>
    <td nowrap>(numA numB -- boolean)</td>
    <td nowrap>(3 0 -- 0)</td></tr>
<tr valign="top"><td>Or</td>
    <td>Logical Or: A || B</td>
    <td nowrap>(numA numB -- boolean)</td>
    <td nowrap>(3 0 -- 1)</td></tr>
<tr valign="top"><td>Not</td>
    <td>Logical Not: !A</td>
    <td nowrap>(num -- boolean)</td>
    <td nowrap>(3 -- 0)</td></tr>
</table>
    
<h1 class="datasheet">Bitwise</h1>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>BitwiseAnd</td>
    <td>Bitwise And: A &amp; B</td>
    <td nowrap>(numA numB -- num)</td>
    <td nowrap>(5 4 -- 4)</td></tr>
<tr valign="top"><td>BitwiseOr</td>
    <td>Bitwise Or: A | B</td>
    <td nowrap>(numA numB -- num)</td>
    <td nowrap>(5 4 -- 5)</td></tr>
<tr valign="top"><td>BitwiseXOr</td>
    <td>Bitwise Exclusive-Or: A ^ B</td>
    <td nowrap>(numA numB -- num)</td>
    <td nowrap>(5 4 -- 1)</td></tr>
<tr valign="top"><td>LogicalShiftLeft</td>
    <td>Logical Shift Left: A << B</td>
    <td nowrap>(numA numB -- num)</td>
    <td nowrap>(4 1 -- 8)</td></tr>
<tr valign="top"><td>LogicalShiftRight</td>
    <td>Logical Shift Right: A >>> B</td>
    <td nowrap>(numA numB -- num)</td>
    <td nowrap>(8 1 -- 4)</td></tr>
<tr valign="top"><td>ArithmeticShiftRight</td>
    <td>Arithmetic Shift Right (sign extension): A >> B</td>
    <td nowrap>(numA numB -- num)</td>
    <td nowrap>(-1 1 -- -1)</td></tr>
</table>

<h1 class="datasheet">String</h1>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>StringAdd</td>
    <td>Concatenate two strings</td>
    <td nowrap>(string string -- string)</td>
    <td nowrap>("ab" "cd" -- "abcd")</td></tr>
<tr valign="top"><td>StringLength</td>
    <td>Returns the length of a string</td>
    <td nowrap>(string -- num)</td>
    <td nowrap>("abc" -- 3)</td></tr>
<tr valign="top"><td>MBStringLength</td>
    <td>Returns the length of a string that contains characters from an extended set such as Unicode.</td>
    <td nowrap>(string -- num)</td>
    <td nowrap>("abc" -- 3)</td></tr>
<tr valign="top"><td>StringExtract</td>
    <td>Substring. Extract <I>count</I> characters from string starting at position <em>index</em>.</td>
    <td nowrap>(count index string -- string)</td>
    <td nowrap>(3 2 "abcde" -- "bcd")</td></tr>
<tr valign="top"><td>MBStringExtract</td>
    <td>Multi-byte Substring. Extract <I>count</I> characters from string starting at position <em>index</em>.</td>
    <td nowrap>(count index string -- string)</td>
    <td nowrap>(3 2 "abcde" -- "bcd")</td></tr>
</table>

<h1 class="datasheet">Type Conversion</h1>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>ToInteger</td>
    <td>Converts the value to an integer</td>
    <td nowrap> ( num -- num)</td>
    <td nowrap> ( 3.2 -- 3 )</tr>
<tr valign="top"><td>ToNumber</td>
    <td>Converts the string value to a number.</td>
    <td nowrap> ( string -- num)</td>
    <td nowrap> ( "3.2" -- 3.2 )</tr>
<tr valign="top"><td>ToString</td>
    <td>Converts the value to a string.</td>
    <td nowrap> ( num -- string)</td>
    <td nowrap> ( 3.2 -- "3.2" )</tr>
<tr valign="top"><td>CharToAscii</td>
    <td>Convert the first character of a string to its ASCII value.</td>
    <td nowrap>(string -- num)</td>
    <td nowrap>("abc" -- 97)</td></tr>
<tr valign="top"><td>MBCharToAscii</td>
    <td>Convert the first character of string to its Unicode value.</td>
    <td nowrap>(string -- num)</td>
    <td nowrap>("abc" -- 61)</td></tr>
<tr valign="top"><td>AsciiToChar</td>
    <td>Convert the ASCII value to the equivalent character.</td>
    <td nowrap>(num -- string)</td>
    <td nowrap>(97 -- "a")</td></tr>
<tr valign="top"><td>MBAsciiToChar</td>
    <td>Convert a Unicode value to the equivalent character.</td>
    <td nowrap>(num -- string)</td>
    <td nowrap>(61 -- "a")</td></tr>
</table>

<h1 class="datasheet">Variables</h1>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>GetVariable</td>
    <td>Push the value for the specified variable on the stack</td>
    <td nowrap>(variableName -- value)</td>
    <td nowrap>("FlashVersion" -- 4)</td></tr>
<tr valign="top"><td>SetVariable</td>
    <td>Set the value of the specified variable</td>
    <td nowrap>(variableName value --)</td>
    <td nowrap>("Var1" 123 --)</td></tr>
<tr valign="top"><td>GetType</td>
    <td>Returns the type of the object or value at the top of the stack.</td>
    <td nowrap>(value -- value type)</td>
    <td nowrap>(--)</td></tr>
<tr valign="top"><td>NewVariable</td>
    <td>Create a new user-defined variable.</td>
    <td nowrap>(name --)</td>
    <td nowrap>("x" --)</td></tr>
<tr valign="top"><td>InitVariable</td>
    <td>Create and initialise a user-defined variable.</td>
    <td nowrap>(value name --)</td>
    <td nowrap>(1 "x" --)</td></tr>
<tr valign="top"><td>NewArray</td>
    <td>Create an array.</td>
    <td nowrap>(value+ count -- array)</td>
    <td nowrap>(1 2 3 4 4 -- array)</td></tr>
<tr valign="top"><td>DeleteVariable</td>
    <td>Deletes a variable, returning true if the variable was deleted, false otherwise.</td>
    <td nowrap>(name -- boolean)</td>
    <td nowrap>("x" -- 1)</td></tr>
<tr valign="top"><td>Delete</td>
    <td>Deletes an object or variable, returning true if the object was deleted, false otherwise.</td>
    <td nowrap>(name -- boolean)</td>
    <td nowrap>("x" -- 1)</td></tr>
</table>

<h1 class="datasheet">Functions</h1>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>ExecuteFunction</td>
    <td>Execute the built-in function.</td>
    <td nowrap>(arg* functionName -- result*)</td>
    <td nowrap>(12.3 "isFinite" -- "1")</td></tr>
<tr valign="top"><td>Return</td>
    <td>Return control from the function.</td>
    <td nowrap>(--)</td>
    <td nowrap>(--)</td></tr>
</table>

<h1 class="datasheet">Objects</h1>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>GetAttribute</td>
    <td>Push the value of an objects attribute on the stack</td>
    <td nowrap>(string string -- value)</td>
    <td nowrap>("Key" "SPACE" -- 32)</td></tr>
<tr valign="top"><td>SetAttribute</td>
    <td>Set the value of a attribute of an object</td>
    <td nowrap>(variable string value --)</td>
    <td nowrap>(&lt;_root&gt; "variable" 1 --)</td></tr>
<tr valign="top"><td>ExecuteMethod</td>
    <td>Execute a method of an object</td>
    <td nowrap>(string string -- value)</td>
    <td nowrap>("Key" "getCode" -- num)</td></tr>
<tr valign="top"><td>NewMethod</td>
    <td>Define a new method for an object</td>
    <td nowrap>&nbsp;</td>
    <td nowrap>&nbsp;</td></tr>
<tr valign="top"><td>NamedObject</td>
    <td>Construct an instance of a built-in object.</td>
    <td nowrap>(arg* count className -- instance)</td>
    <td nowrap>("My String" 1 "String" -- instance)</td></tr>
<tr valign="top"><td>NewObject</td>
    <td>Define a new class.</td>
    <td nowrap>((name value)* count -- instance)</td>
    <td nowrap>("Account" "123456" 1 -- value)</td></tr>
<tr valign="top"><td>Enumerate</td>
    <td>Enumerate through the attributes of the object referenced by the name of the variable on the stack.</td>
    <td nowrap>( "var" -- )</td>
    <td nowrap>( -- )</td></tr>
<tr valign="top"><td>EnumerateObject</td>
    <td>Enumerate through the attributes of the object on the stack.</td>
    <td nowrap>( "var" -- )</td>
    <td nowrap>( -- )</td></tr>
</table>

<h1 class="datasheet">Movie Control</h1>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>GetTarget</td>
    <td>Returns a string representing the path to the movie clip in which the current action is executed.</td>
    <td nowrap>(-- clipName )</td>
    <td nowrap>( -- "_root/MovieClip")</td></tr>
<tr valign="top"><td>SetTarget2</td>
    <td>Change the context of the Flash Player so subsequent actions are applied to the movie clip, <i>clipName</i>.</td>
    <td nowrap>(clipName -- )</td>
    <td nowrap>("MovieClip" --)</td></tr>
<tr valign="top"><td>GetProperty</td>
    <td>Push the value of the specified property on the stack. Properties are identified by reserved values, see the FSPush class for more details.</td>
    <td nowrap>(value -- value)</td>
    <td nowrap>( &lt;_totalframes&gt; -- 36 )</td></tr>
<tr valign="top"><td>SetProperty</td>
    <td>Set the value of a property</td>
    <td nowrap>(value propertyName --)</td>
    <td nowrap>( 8000 &lt;_width&gt; -- )</td></tr>
<tr valign="top"><td>CloneSprite</td>
    <td>Duplicate a movie clip <i>clipName</i>, on the display list layer <i>depth</i> with the name <i>newName</i>.</td>
    <td nowrap>( depth clipName newName --)</td>
    <td nowrap>( 19 "_root/MovieClip" "newClip" -- )</td></tr>
<tr valign="top"><td>RemoveSprite</td>
    <td>Delete a movie clip</td>
    <td nowrap>( clipName --)</td>
    <td nowrap>( "_root/MovieClip" -- )</td></tr>
<tr valign="top"><td>StartDrag</td>
    <td>Starts dragging a movie clip with an optional constraining rectangle defined by the corner points (x1,y1), (x2,y2).</td>
    <td nowrap>( x1 y1 x2 y2 1 clipName --)<br><br>( 0 clipName --)</td>
    <td nowrap>( 0 0 400 400 1 "movieClip" - )<br><br>( 0 "movieClip" - )</td></tr>
<tr valign="top"><td>EndDrag</td>
    <td>Stops dragging a movie clip</td>
    <td nowrap>(--)</td>
    <td nowrap>&nbsp;</td></tr>
<tr valign="top"><td>NextFrame</td>
    <td>Go to the next frame of the current movie</td>
    <td nowrap>(--)</td>
    <td nowrap>&nbsp;</td></tr>
<tr valign="top"><td>PreviousFrame</td>
    <td>Go to the previous frame of the current movie</td>
    <td nowrap>(-- )</td&nbsp;>
    <td nowrap>&nbsp;</td></tr>
<tr valign="top"><td>Play</td>
    <td>Start playing the current movie at the current frame</td>
    <td nowrap>(--)</td>
    <td nowrap>&nbsp;</td></tr>
<tr valign="top"><td>Stop</td>
    <td>Stop playing the current movie</td>
    <td nowrap>(--)</td>
    <td nowrap>&nbsp;</td></tr>
<tr valign="top"><td>ToggleQuality</td>
    <td>Toggle the movie between high and low quality</td>
    <td nowrap>(--)</td>
    <td nowrap>&nbsp;</td></tr>
<tr valign="top"><td>StopSounds</td>
    <td>Stop playing all sounds</td>
    <td nowrap>(--)</td>
    <td nowrap>&nbsp;</td></tr>
</table>    
    
<h1 class="datasheet">ActionScript 2.0</h1>

<p>Starting with Flash 6 Macromedia extended the syntax of ActionScript to make 
it more object-oriented, moving the language closer to Java than JavaScript. 
Several actions were added to support the new keywords introduced into ActionScript 
2.0.</p>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>InstanceOf</td>
    <td>Return true or false to the stack if the object can be created using the constructor function.</td>
    <td nowrap>( object function -- true | false)</td>
    <td nowrap>&nbsp;</td></tr>
<tr valign="top"><td>Implements</td>
    <td>Identifies a class implements a defined interface.</td>
    <td nowrap>( (function) count function --)</td>
    <td nowrap>&nbsp;</td></tr>
<tr valign="top"><td>Extends</td>
    <td>Identifies that a class inherits from a class - used to increase the execution speed of ActionScript code.</td>
    <td nowrap>( subclass superclass --)</td>
    <td nowrap>&nbsp;</td></tr>
<tr valign="top"><td>Cast</td>
    <td>Casts the type of an object on the stack, returning the object if it is the same type as the constructor function, null otherwise.</td>
    <td nowrap>(function object -- object | null)</td>
    <td nowrap>&nbsp;</td></tr>
<tr valign="top"><td>Throw</td>
    <td>Throw an exception.</td>
    <td nowrap>(--)</td>
    <td nowrap>&nbsp;</td></tr>
</table>
    
<h1 class="datasheet">Miscellaneous</h1>

<table class="actions">

<tr><th>Action</th><th>Description</th><th>Stack Notation</th><th>Example</th></tr>

<tr valign="top"><td>Trace</td>
    <td>Append value to debugging window</td>
    <td nowrap>(value --)</td>
    <td nowrap>("X = 3" --)</td></tr>
<tr valign="top"><td>GetTime</td>
    <td>Push the number of milliseconds that have elapsed since the player started on the stack.</td>
    <td nowrap>(-- num)</td>
    <td nowrap>(-- 1274832)</td></tr>
<tr valign="top"><td>RandomNumber</td>
    <td>Push a random number on the stack.</td>
    <td nowrap>(maximumValue -- num)</td>
    <td nowrap>(10 -- 3)</td></tr>
</table>

<p><b>Notes</b></p>

<ul>
<li>The Add action was updated in Flash 5 so it can be used to perform arithmetic add and string concatenation depending on whether the values on the stack can be interpreted as numeric values or strings. The original add action (Code = 10) should not be used.</li>

<li>For the division operation, if a divide by zero error occurs then the string "#ERROR" is pushed onto the stack.</li>

<li>The Equals and Less actions were updated in Flash 5 so it can be used to perform arithmetic and string comparison depending on whether the values on the stack can be interpreted as numeric values or strings. The original Less action (code = 15) and Equals action (code = 14) should not be used.</li>

<li>Type conversion of string characters is now handled by the String object, introduced in Flash 5. The ToInteger action is now supported by the Math object also introduced in Flash 5. The type conversion functions are only included for completeness.</li>

<li>Flash version 5 supports built-in and user defined objects. For a full description of the objects supported please consult an ActionScript 5 reference guide.</li>
</ul>

<h1 class="datasheet">Examples</h1>

<p>The FSActionObject class defines a series of constants that lists the type of actions supported in the current release. Actions may be created by specifying the action type in the constructor:</p>

<pre>
FSAction anAction = new FSAction(FSAction.Add);
</pre>

<p>The FSPush class is used to push values onto the Flash Player's stack before an action is executed. For example to execute the expression (1+2)*3 when a frame is displayed the following sequence of actions are created:</p>

<pre>
FSDoAction frameAction = new FSDoAction();

frameAction.add(new FSPush(1));
frameAction.add(new FSPush(2));
frameAction.add(new FSAction(FSAction.Add));
frameAction.add(new FSPush(3));
frameAction.add(new FSAction(FSAction.Multiply));
</pre>

<p>The Flash Player also supported classes and object that represent different complex data types and system resources such as the mouse. These objects and the functions they support are referenced by name. String containing the names and the values (and number) of the arguments required are pushed onto the stack:</p>

<pre>
// Push the arguments followed by the number of arguments onto the stack

frameAction.add(new FSPush(aValue));
frameAction.add(new FSPush(aValue));
frameAction.add(new FSPush(2));

// Place the name on the stack then execute the function.

frameAction.add(new FSPush("FunctionName"));
frameAction.add(new FSAction(FSAction.ExecuteFunction));
</pre>

<p>To execute a method on a given object a reference to the object is retrieved and the name of the method and any arguments are specified. For example to play a movie clip starting at a named frame:</p>

<pre>
// Push the arguments followed by the number of arguments onto the stack

frameAction.add(new FSPush("frameName"));
frameAction.add(new FSPush(1));

// Get a reference to the object.

frameAction.add(new FSPush("_root"));
frameAction.add(new FSPush("movieClip"));
frameAction.add(new FSAction(FSAction.GetAttribute));

// Place the name of the method on the stack then execute it.

frameAction.add(new FSPush("gotoAndPlay"));
frameAction.add(new FSAction(FSAction.ExecuteMethod));
</pre>

<p>Note: The FSPush class allows more than one value to be pushed onto the stack at a time. In the above examples separate FSPush objects are created to make the code a little more readable.</p>

*/
public class FSAction extends FSActionObject {

    /** Type identifying the end of a sequence of actions. */
    public static final int End = 0;

    /** Type identifying a NextFrame stack-based action. */
    public static final int NextFrame = 4;

    /** Type identifying a PrevFrame stack-based action. */
    public static final int PrevFrame = 5;

    /** Type identifying a Play stack-based action. */
    public static final int Play = 6;

    /** Type identifying a Stop stack-based action. */
    public static final int Stop = 7;

    /** Type identifying a ToggleQuality stack-based action. */
    public static final int ToggleQuality = 8;

    /** Type identifying a StopSounds stack-based action. */
    public static final int StopSounds = 9;

    public static final int IntegerAdd = 10;

    public static final int Subtract = 11;

    public static final int Multiply = 12;

    public static final int Divide = 13;

    public static final int IntegerEquals = 14;

    public static final int IntegerLess = 15;

    public static final int And = 16;

    public static final int Or = 17;

    public static final int Not = 18;

    public static final int StringEquals = 19;

    public static final int StringLength = 20;

    public static final int StringExtract = 21;

    public static final int Pop = 23;

    public static final int ToInteger = 24;

    public static final int GetVariable = 28;

    public static final int SetVariable = 29;

    public static final int SetTarget2 = 32;

    public static final int StringAdd = 33;

    public static final int GetProperty = 34;

    public static final int SetProperty = 35;

    public static final int CloneSprite = 36;

    public static final int RemoveSprite = 37;

    public static final int Trace = 38;

    public static final int StartDrag = 39;

    public static final int EndDrag = 40;

    public static final int StringLess = 41;

    public static final int RandomNumber = 48;

    public static final int MBStringLength = 49;

    public static final int CharToAscii = 50;

    public static final int AsciiToChar = 51;

    public static final int GetTime = 52;

    public static final int MBStringExtract = 53;

    public static final int MBCharToAscii = 54;

    public static final int MBAsciiToChar = 55;

    public static final int DeleteVariable = 58;

    public static final int Delete = 59;

    public static final int InitVariable = 60;

    public static final int ExecuteFunction = 61;

    public static final int Return = 62;

    public static final int Modulo = 63;

    public static final int NamedObject = 64;

    public static final int NewVariable = 65;

    public static final int NewArray = 66;

    public static final int NewObject = 67;

    public static final int GetType = 68;

    public static final int GetTarget = 69;

    public static final int Enumerate = 70;

    public static final int Add = 71;

    public static final int Less = 72;

    public static final int Equals = 73;

    public static final int ToNumber = 74;

    public static final int ToString = 75;

    public static final int Duplicate = 76;

    public static final int Swap = 77;

    public static final int GetAttribute = 78;

    public static final int SetAttribute = 79;

    public static final int Increment = 80;

    public static final int Decrement = 81;

    public static final int ExecuteMethod = 82;

    public static final int NewMethod = 83;

    public static final int BitwiseAnd = 96;

    public static final int BitwiseOr = 97;

    public static final int BitwiseXOr = 98;

    public static final int LogicalShiftLeft = 99;

    public static final int ArithmeticShiftRight = 100;

    public static final int LogicalShiftRight = 101;

    public static final int InstanceOf = 84;

    public static final int EnumerateObject = 85;

    public static final int StrictEquals = 102;

    public static final int Greater = 103;

    public static final int StringGreater = 104;

    public static final int Throw = 42;

    public static final int Cast = 43;

    public static final int Implements = 44;

    public static final int Extends = 105;

    static String[] names = { "End", "", "", "", "NextFrame", "PrevFrame", "Play", "Stop", "ToggleQuality", "StopSounds", "IntegerAdd", "Subtract", "Multiply", "Divide", "IntegerEquals", "IntegerLess", "And", "Or", "Not", "StringEquals", "StringLength", "StringExtract", "", "Pop", "ToInteger", "", "", "", "GetVariable", "SetVariable", "", "", "SetTarget2", "StringAdd", "GetProperty", "SetProperty", "CloneSprite", "RemoveSprite", "Trace", "StartDrag", "EndDrag", "StringLess", "Throw", "Cast", "Implements", "", "", "", "RandomNumber", "MBStringLength", "CharToAscii", "AsciiToChar", "GetTime", "MBStringExtract", "MBCharToAscii", "MBAsciiToChar", "", "", "DeleteVariable", "Delete", "InitVariable", "ExecuteFunction", "Return", "Modulo", "NamedObject", "NewVariable", "NewArray", "NewObject", "GetType", "GetTarget", "Enumerate", "Add", "Less", "Equals", "ToNumber", "ToString", "Duplicate", "Swap", "GetAttribute", "SetAttribute", "Increment", "Decrement", "ExecuteMethod", "NewMethod", "InstanceOf", "EnumerateObject", "", "", "", "", "", "", "", "", "", "", "BitwiseAnd", "BitwiseOr", "BitwiseXOr", "LogicalShiftLeft", "ArithmeticShiftRight", "LogicalShiftRight", "StrictEquals", "Greater", "StringGreater", "Extends", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };

    private static final FSAction[] actions = { new FSAction(FSAction.End), new FSAction(1), new FSAction(2), new FSAction(3), new FSAction(FSAction.NextFrame), new FSAction(FSAction.PrevFrame), new FSAction(FSAction.Play), new FSAction(FSAction.Stop), new FSAction(FSAction.ToggleQuality), new FSAction(FSAction.StopSounds), new FSAction(FSAction.IntegerAdd), new FSAction(FSAction.Subtract), new FSAction(FSAction.Multiply), new FSAction(FSAction.Divide), new FSAction(FSAction.IntegerEquals), new FSAction(FSAction.IntegerLess), new FSAction(FSAction.And), new FSAction(FSAction.Or), new FSAction(FSAction.Not), new FSAction(FSAction.StringEquals), new FSAction(FSAction.StringLength), new FSAction(FSAction.StringExtract), new FSAction(22), new FSAction(FSAction.Pop), new FSAction(FSAction.ToInteger), new FSAction(25), new FSAction(26), new FSAction(27), new FSAction(FSAction.GetVariable), new FSAction(FSAction.SetVariable), new FSAction(30), new FSAction(31), new FSAction(FSAction.SetTarget2), new FSAction(FSAction.StringAdd), new FSAction(FSAction.GetProperty), new FSAction(FSAction.SetProperty), new FSAction(FSAction.CloneSprite), new FSAction(FSAction.RemoveSprite), new FSAction(FSAction.Trace), new FSAction(FSAction.StartDrag), new FSAction(FSAction.EndDrag), new FSAction(FSAction.StringLess), new FSAction(FSAction.Throw), new FSAction(FSAction.Cast), new FSAction(FSAction.Implements), new FSAction(45), new FSAction(46), new FSAction(47), new FSAction(FSAction.RandomNumber), new FSAction(FSAction.MBStringLength), new FSAction(FSAction.CharToAscii), new FSAction(FSAction.AsciiToChar), new FSAction(FSAction.GetTime), new FSAction(FSAction.MBStringExtract), new FSAction(FSAction.MBCharToAscii), new FSAction(FSAction.MBAsciiToChar), new FSAction(56), new FSAction(57), new FSAction(FSAction.DeleteVariable), new FSAction(FSAction.Delete), new FSAction(FSAction.InitVariable), new FSAction(FSAction.ExecuteFunction), new FSAction(FSAction.Return), new FSAction(FSAction.Modulo), new FSAction(FSAction.NamedObject), new FSAction(FSAction.NewVariable), new FSAction(FSAction.NewArray), new FSAction(FSAction.NewObject), new FSAction(FSAction.GetType), new FSAction(FSAction.GetTarget), new FSAction(FSAction.Enumerate), new FSAction(FSAction.Add), new FSAction(FSAction.Less), new FSAction(FSAction.Equals), new FSAction(FSAction.ToNumber), new FSAction(FSAction.ToString), new FSAction(FSAction.Duplicate), new FSAction(FSAction.Swap), new FSAction(FSAction.GetAttribute), new FSAction(FSAction.SetAttribute), new FSAction(FSAction.Increment), new FSAction(FSAction.Decrement), new FSAction(FSAction.ExecuteMethod), new FSAction(FSAction.NewMethod), new FSAction(FSAction.InstanceOf), new FSAction(FSAction.EnumerateObject), new FSAction(86), new FSAction(87), new FSAction(88), new FSAction(89), new FSAction(90), new FSAction(91), new FSAction(92), new FSAction(93), new FSAction(94), new FSAction(95), new FSAction(FSAction.BitwiseAnd), new FSAction(FSAction.BitwiseOr), new FSAction(FSAction.BitwiseXOr), new FSAction(FSAction.LogicalShiftLeft), new FSAction(FSAction.ArithmeticShiftRight), new FSAction(FSAction.LogicalShiftRight), new FSAction(FSAction.StrictEquals), new FSAction(FSAction.Greater), new FSAction(FSAction.StringGreater), new FSAction(FSAction.Extends), new FSAction(106), new FSAction(107), new FSAction(108), new FSAction(109), new FSAction(110), new FSAction(111), new FSAction(112), new FSAction(113), new FSAction(114), new FSAction(115), new FSAction(116), new FSAction(117), new FSAction(118), new FSAction(119), new FSAction(120), new FSAction(121), new FSAction(122), new FSAction(123), new FSAction(124), new FSAction(125), new FSAction(126), new FSAction(127) };

    static FSAction getInstance(int type) {
        return actions[type];
    }

    /** Factory method for generating an FSAction object representing the end of a sequence of actions. */
    public static FSAction End() {
        return actions[FSAction.End];
    }

    /** Factory method for generating an FSAction object representing a NextFrame action. */
    public static FSAction NextFrame() {
        return actions[FSAction.NextFrame];
    }

    /** Factory method for generating an FSAction object representing a PrevFrame action. */
    public static FSAction PrevFrame() {
        return actions[FSAction.PrevFrame];
    }

    /** Factory method for generating an FSAction object representing a Play action. */
    public static FSAction Play() {
        return actions[FSAction.Play];
    }

    /** Factory method for generating an FSAction object representing a Stop action. */
    public static FSAction Stop() {
        return actions[FSAction.Stop];
    }

    /** Factory method for generating an FSAction object representing a ToggleQuality action. */
    public static FSAction ToggleQuality() {
        return actions[FSAction.ToggleQuality];
    }

    /** Factory method for generating an FSAction object representing a StopSounds action. */
    public static FSAction StopSounds() {
        return actions[FSAction.StopSounds];
    }

    public static FSAction Subtract() {
        return actions[FSAction.Subtract];
    }

    public static FSAction Multiply() {
        return actions[FSAction.Multiply];
    }

    public static FSAction Divide() {
        return actions[FSAction.Divide];
    }

    public static FSAction And() {
        return actions[FSAction.And];
    }

    public static FSAction Or() {
        return actions[FSAction.Or];
    }

    public static FSAction Not() {
        return actions[FSAction.Not];
    }

    public static FSAction StringEquals() {
        return actions[FSAction.StringEquals];
    }

    public static FSAction StringLength() {
        return actions[FSAction.StringLength];
    }

    public static FSAction StringExtract() {
        return actions[FSAction.StringExtract];
    }

    public static FSAction Pop() {
        return actions[FSAction.Pop];
    }

    public static FSAction ToInteger() {
        return actions[FSAction.ToInteger];
    }

    public static FSAction GetVariable() {
        return actions[FSAction.GetVariable];
    }

    public static FSAction SetVariable() {
        return actions[FSAction.SetVariable];
    }

    public static FSAction SetTarget2() {
        return actions[FSAction.SetTarget2];
    }

    public static FSAction StringAdd() {
        return actions[FSAction.StringAdd];
    }

    public static FSAction GetProperty() {
        return actions[FSAction.GetProperty];
    }

    public static FSAction SetProperty() {
        return actions[FSAction.SetProperty];
    }

    public static FSAction CloneSprite() {
        return actions[FSAction.CloneSprite];
    }

    public static FSAction RemoveSprite() {
        return actions[FSAction.RemoveSprite];
    }

    public static FSAction Trace() {
        return actions[FSAction.Trace];
    }

    public static FSAction StartDrag() {
        return actions[FSAction.StartDrag];
    }

    public static FSAction EndDrag() {
        return actions[FSAction.EndDrag];
    }

    public static FSAction StringLess() {
        return actions[FSAction.StringLess];
    }

    public static FSAction RandomNumber() {
        return actions[FSAction.RandomNumber];
    }

    public static FSAction MBStringLength() {
        return actions[FSAction.MBStringLength];
    }

    public static FSAction CharToAscii() {
        return actions[FSAction.CharToAscii];
    }

    public static FSAction AsciiToChar() {
        return actions[FSAction.AsciiToChar];
    }

    public static FSAction GetTime() {
        return actions[FSAction.GetTime];
    }

    public static FSAction MBStringExtract() {
        return actions[FSAction.MBStringExtract];
    }

    public static FSAction MBCharToAscii() {
        return actions[FSAction.MBCharToAscii];
    }

    public static FSAction MBAsciiToChar() {
        return actions[FSAction.MBAsciiToChar];
    }

    public static FSAction DeleteVariable() {
        return actions[FSAction.DeleteVariable];
    }

    public static FSAction Delete() {
        return actions[FSAction.Delete];
    }

    public static FSAction InitVariable() {
        return actions[FSAction.InitVariable];
    }

    public static FSAction ExecuteFunction() {
        return actions[FSAction.ExecuteFunction];
    }

    public static FSAction Return() {
        return actions[FSAction.Return];
    }

    public static FSAction Modulo() {
        return actions[FSAction.Modulo];
    }

    public static FSAction NamedObject() {
        return actions[FSAction.NamedObject];
    }

    public static FSAction NewVariable() {
        return actions[FSAction.NewVariable];
    }

    public static FSAction NewArray() {
        return actions[FSAction.NewArray];
    }

    public static FSAction NewObject() {
        return actions[FSAction.NewObject];
    }

    public static FSAction GetType() {
        return actions[FSAction.GetType];
    }

    public static FSAction GetTarget() {
        return actions[FSAction.GetTarget];
    }

    public static FSAction Enumerate() {
        return actions[FSAction.Enumerate];
    }

    public static FSAction Add() {
        return actions[FSAction.Add];
    }

    public static FSAction Less() {
        return actions[FSAction.Less];
    }

    public static FSAction Equals() {
        return actions[FSAction.Equals];
    }

    public static FSAction ToNumber() {
        return actions[FSAction.ToNumber];
    }

    public static FSAction ToString() {
        return actions[FSAction.ToString];
    }

    public static FSAction Duplicate() {
        return actions[FSAction.Duplicate];
    }

    public static FSAction Swap() {
        return actions[FSAction.Swap];
    }

    public static FSAction GetAttribute() {
        return actions[FSAction.GetAttribute];
    }

    public static FSAction SetAttribute() {
        return actions[FSAction.SetAttribute];
    }

    public static FSAction Increment() {
        return actions[FSAction.Increment];
    }

    public static FSAction Decrement() {
        return actions[FSAction.Decrement];
    }

    public static FSAction ExecuteMethod() {
        return actions[FSAction.ExecuteMethod];
    }

    public static FSAction NewMethod() {
        return actions[FSAction.NewMethod];
    }

    public static FSAction BitwiseAnd() {
        return actions[FSAction.BitwiseAnd];
    }

    public static FSAction BitwiseOr() {
        return actions[FSAction.BitwiseOr];
    }

    public static FSAction BitwiseXOr() {
        return actions[FSAction.BitwiseXOr];
    }

    public static FSAction LogicalShiftLeft() {
        return actions[FSAction.LogicalShiftLeft];
    }

    public static FSAction ArithmeticShiftRight() {
        return actions[FSAction.ArithmeticShiftRight];
    }

    public static FSAction LogicalShiftRight() {
        return actions[FSAction.LogicalShiftRight];
    }

    public static FSAction InstanceOf() {
        return actions[FSAction.InstanceOf];
    }

    public static FSAction EnumerateObject() {
        return actions[FSAction.EnumerateObject];
    }

    public static FSAction Greater() {
        return actions[FSAction.Greater];
    }

    public static FSAction StringGreater() {
        return actions[FSAction.StringGreater];
    }

    public static FSAction StrictEquals() {
        return actions[FSAction.StrictEquals];
    }

    public static FSAction Cast() {
        return actions[FSAction.Cast];
    }

    public static FSAction Implements() {
        return actions[FSAction.Implements];
    }

    public static FSAction Throw() {
        return actions[FSAction.Throw];
    }

    public static FSAction Extends() {
        return actions[FSAction.Extends];
    }

    /**
     * Construct an FSAction object, initalizing it with values decoded from an
     * encoded object.
     * 
     * @param coder an FSCoder containing the binary data.
     */
    public FSAction(FSCoder coder) {
        super(0);
        decode(coder);
    }

    /** 
     * Constructs a stack-based action with the specified type.
     * 
     * @param aType the code used to denote the type of action performed.
     */
    public FSAction(int aType) {
        super(aType);
    }

    /**
     * Constructs an FSAction object by copying values from an existing 
     * object.
     *
     * @param obj an FSAction object.
     */
    public FSAction(FSAction obj) {
        super(obj);
    }

    /**
     * Returns a string identifying the type of stack-based action that the 
     * object represents.
     * 
     * @return a string containing the name of the action.
     */
    public String name() {
        return names[type];
    }

    public void appendDescription(StringBuffer buffer, int depth) {
        buffer.append(name());
    }

    public void encode(FSCoder coder) {
        super.encode(coder);
        coder.endObject(name());
    }

    public void decode(FSCoder coder) {
        super.decode(coder);
        coder.endObject(name());
    }
}
