package net.sourceforge.tekkenmoves;

import javax.swing.ImageIcon;

import net.sourceforge.tekkenmoves.util.JoystickMapper;

public class MoveLogic{
	private static JoystickMapper joyStick = JoystickMapper.getInstance();

	public MoveLogic()
	{
		System.out.print("this class only excepts moves as strings");
	}
	public MoveLogic(Character characterX)
	{
		System.out.print("this class only excepts moves as strings");
		//parseCharacter(characterX);
	}
	public MoveLogic(String moves)
	{
		parseMove(moves);
	}
	
	public static ImageIcon[] parseMove(String moves)
	{
		//takes the move sequence as string and returns an array representing the picture order for that move
		return moveWordParser(moves);
	}
	public static Character parseCharacter(Character characterX)
	{
		//NOT SURE i can do this since i didnt think u could
		//edit objects when using the enhanced for loop?? need to ask kevin
		for(Move move : characterX.getMoves())
		{
			parseMove(move.getMoveSequence());
		}
		return characterX;
	}
	
	static private joyStick placePic(char letter)
	{
		switch (letter)
		{
			//LT means LINK TO
		case '1': return joyStick.get("square");
		//LT: left punch
		case '2': return joyStick.get("triangle");
		//LT: right punch
		case '3': return joyStick.get("x");
		//LT: left kick
		case '4': return joyStick.get("circle");
		//LT: right kick
		case 'f': return joyStick.get("f");
		//LT: tap forward arrow once
		case 'b': return joyStick.get("b");
		//LT: tap back arrow once
		case 'd': return joyStick.get("d");
		//LT: tap down arrow once
		case 'u': return joyStick.get("u");
		//LT: tap up arrow once
		case 'F': return joyStick.get("F");
		//LT: hold forward arrow once
		case 'B': return joyStick.get("B");;
		//LT: hold back arrow once
		case 'D': return joyStick.get("D");
		//LT: hold down arrow once
		case 'U': return joyStick.get("U");
		//LT: hold up arrow once
		case 'q': return joyStick.get("q");
		//LT: quarter circle down forward
		case 'h': return joyStick.get("h");
		//LT: half circle back down forward
		case 'Q': return joyStick.get("Q");
		//LT: quarter circle down backward
		case 'H': return joyStick.get("H");
		//LT: half circle forward down backward
		case ',': return joyStick.get(",");
		//LT: comma (,) (pressed sequentially)
		case 'N': return joyStick.get("N");
		//LT: neutral (*)
		case '+': return joyStick.get("+");
		//LT: plus symbol (+) (done together)
		case '_': return joyStick.get("_");
		//LT: underscore (_) (logical or)
		case '(': return joyStick.get("(");
		//LT: opening parenthesis
		case ')': return joyStick.get(")");
		//LT: closing parenthesis
		case '!': return joyStick.get("!");
		//LT: (*) optional delay
		case '#': return joyStick.get("#");
		//LT: HOLD
		case '.': return joyStick.get(".");
		//LT: period for timed combos
		case 'Z': return joyStick.get("Z");
		//LT: BLANK
		//shouldnt need this N or end of string implys it//case '$': PicFilename[index]="src/main/resources/img.seq/31_RELEASE.jpg";//LT: RELEASE
		
		}
	}
	static private ImageIcon[] moveWordParser(String moveString)
	{
		ImageIcon[] picArray= new ImageIcon[moveString.length()];
		for(int x=1;x<moveString.length()+1;x++)
			{
			// string of control logic chars   #()
			//ignoring [] {and text inside} : but they shouldn't be in the XML file anyways
			if (moveString.charAt(x+1) != null)
			if (moveString.charAt(x) == '#' || moveString.charAt(x+1) == '#')
				{
					picArray[x] = placePic('#');
					picArray[x+1] = placePic(moveString.charAt((x-1)));
					++x;
				}	
				if (moveString.charAt(x) == 'H' && moveString.charAt(x+1) == 'F')
				{
					picArray[x] = placePic('h');
					picArray[x+1] = placePic('Z');
					++x;
				}else if(moveString.charAt(x) == 'H' && moveString.charAt(x+1) == 'B')
				{
					picArray[x] = placePic('H');
					picArray[x+1] = placePic('Z');
					++x;
				}
				if (moveString.charAt(x) == 'Q' && moveString.charAt(x+1) == 'F')
				{
					picArray[x] = placePic('q');
					picArray[x+1] = placePic('z');
					++x;
				}else if(moveString.charAt(x) == 'Q' && moveString.charAt(x+1) == 'B')
				{
					picArray[x] = placePic('Q');
					picArray[x+1] = placePic('Z');
					++x;
				}

				picArray[x] = placePic(moveString.charAt(x))
			}
		return picArray;
	}
}