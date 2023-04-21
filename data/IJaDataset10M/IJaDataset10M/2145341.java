/*

Copyright (C) 2001 Kevin E. Gilpin (kevin.gilpin@alum.mit.edu)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License (http://www.gnu.org/copyleft/gpl.html)
for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/

package agonism.ce;

import java.lang.reflect.Field;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.StringTokenizer;

public class Debug
{
	public static final int MAX_TRACE_IDS = 1000;
	public static boolean isDebugging = false;

	public static PrintStream stream = System.out;

	private static boolean[] s_traceIDs = new boolean[MAX_TRACE_IDS];
	private static int s_traceID = 0;

	public static void enableNamedClasses(String classes, String separator)
	{
		StringTokenizer tok = new StringTokenizer(classes, separator);
		Class[] classesArray = new Class[tok.countTokens()];
		for ( int i=0;tok.hasMoreTokens();i++ )
		{
			String className = tok.nextToken();
			try {
				System.out.println("Tracing " + className);
				Class clazz = Class.forName(className);
				classesArray[i] = clazz;
			}
			catch (ClassNotFoundException x) {
				trace(x);
				classesArray[i] = null;
			}
		}
		enableClasses(classesArray);
	}

	public static void enableClass(Class clazz)
	{
		enableClasses(new Class[]{ clazz });
	}

	public static void enableClasses(Class[] classes)
	{
		for ( int i=0;i<classes.length;i++ )
		{
			Class clazz = classes[i];
			if ( clazz != null )
			{
				try {
					Field field = clazz.getField("TRACE_ID");
					int traceID = field.getInt(null);
					setTracing(traceID, true);
				}
				catch (Exception x) {
					trace(x);
				}
			}
		}
	}
	
	public static int getTraceID()
	{
		if ( s_traceID == MAX_TRACE_IDS )
			s_traceID = 0;
		return s_traceID++;
	}
	
	public static void setTracing(int traceID, boolean trace)
	{
		s_traceIDs[traceID] = trace;
	}
	
	public static boolean isTracing(int traceID)
	{
		return isDebugging || s_traceIDs[traceID];
	}
	
	public static void assert(boolean b, String msg, int param)
	{
		if ( !b )
		{
			msg = MessageFormat.format(msg, new Object[]{ new Integer(param) });
			assert(false, msg);
		}
	}
	
	public static void assert(boolean b, String msg, double param)
	{
		if ( !b )
		{
			msg = MessageFormat.format(msg, new Object[]{ new Double(param) });
			assert(false, msg);
		}
	}
	
	public static void assert(boolean b, String msg, Object param)
	{
		if ( !b )
		{
			msg = MessageFormat.format(msg, new Object[]{ param });
			assert(false, msg);
		}
	}
	
	public static void assert(boolean b, String msg, Object param1, Object param2)
	{
		if ( !b )
		{
			msg = MessageFormat.format(msg, new Object[]{ param1.toString(), param2.toString() });
			assert(false, msg);
		}
	}
	
	public static void assert(boolean b, String msg, Object param1, Object param2, Object param3)
	{
		if ( !b )
		{
			msg = MessageFormat.format(msg, new Object[]{ param1.toString(), param2.toString(), param3.toString() });
			assert(false, msg);
		}
	}
	
	public static void assert(boolean b, String msg)
	{
		if ( !b )
		{
			throw new RuntimeException(msg);
		}
	}

	public static void trace(String msg)
	{
		stream.println(msg);
	}
	
	public static void trace(Traceable obj, String msg)
	{
		if ( isTracing(obj.getTraceID()) )
			 trace(msg);
	}
	
	public static void trace(Throwable x)
	{
		x.printStackTrace(Debug.stream);
	}
	
	public static void checkNull(Object obj, String methodName, String argument)
	{
		if ( obj == null )
			throw new NullPointerException("Argument '" + argument + "' is null in method '" + methodName + "'");
	}

	public static String printString(IPrintable printable)
	{
		StringWriter writer = new StringWriter();
		printable.toString(new PrintWriter(writer));
		writer.flush();
		return writer.toString();
	}
}
