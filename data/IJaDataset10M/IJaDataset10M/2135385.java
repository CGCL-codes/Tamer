package samples.paper;

import java.awt.Color;
import java.util.Date;
import java.util.ArrayList;
import java.io.*;

/*OUTPUT: "25",
         "25",
         "-1",
         "true",
         "true",
         "line1",
         "line2",
         "line3",
         "3",
         "6",
         "true",
         "3",
         "6",
         "true",
         "Color=Red",           
         "oak"  */
public class Snippets
{
   
//   public static void print(boolean b)
//   {
//      System.out.println(b);      
//   }

   public static void print(Object expected)
   {      
      System.out.println(expected);      
   }
   
   struct XYZ
   { 
      int x,y,z;
   }

   struct XY
   { 
      int x,y;
   }


   static int innerProduct1(XY a, XY b) {
      return a.x * b.x + a.y * b.y;
   }


   static int innerProduct2(struct {int x, y; } a, struct { int y,x; } b)
   {
      return a.x * b.x + a.y * b.y;
   }


   public static void t1()
   {
      int xmas1 = innerProduct1(
         new Object(){ int x=3,y=5; int a=3;},
         new Object(){ int x=5,y=2;}
      );
      
      print(xmas1);
      
      int xmas2 = innerProduct2(
         new Object(){ int x=3,y=5; int a=3;},
         new Object(){ int x=5,y=2;}
      );

      print(xmas2);
   }
   
   static void exhaust(struct { int read() throws Exception; } source) throws Exception {
      while (source.read() >= 0)
         ;
   }
   
   public static void t2() throws Exception 
   {
      StringReader r = new StringReader("ab");
      exhaust(r);
      print(r.read());
   }

   struct ErrorItem
   {
      int severity(); 
      String description;
      final int lineNumber; 
      String where()
      {   
         return lineNumber+ ":" +description; 
      } 
      
      constructor(String d, int l); 
   }


   ErrorItem bump(ErrorItem e, int delta)
   {
      return e.constructor(e.description, e.lineNumber + delta);
   }


   public static void t74()
   {
      struct {
         Object me();
      }
      a = new ArrayList() { Object me() { return this;} },
      b = new Date() { Object me() { return this;} };
      
      print(a.me() == a);
      print(b.me() == b);
   }


   struct LineReader
   {
      int read() throws Exception;
      
      String readLine() throws Exception
      {  
         String s= "";
         for (int c = read(); c >= 0 && c != '\n'; c = read())
            s += (char) c;
         return s;
      }
   }

   static void f(java.io.Reader r) throws Exception
   {
      System.out.println(((LineReader) r).readLine());
   }
   
   static void t121() throws Exception
   {
      Reader r = new StringReader("line1\nline2\nline3");
      f(r);
      f(r);
      f(r);
   }



   struct List
   {
     int head();
     List tail();
   }

   struct MutableList
   { 
      int head(); 
      MutableList tail(); 
      void tail(MutableList tail); 
   } 

   struct ReversableMutableList
   {
      ReversableMutableList reverse();
      int head();
      ReversableMutableList tail();
      void tail(ReversableMutableList tail);
   }



   struct TCircle 
   {
      double radius();
      double diameter() 
      {
         return 2*radius(); 
      }
   }

   struct TRed 
   {
      Color color() { return Color.RED; }
   }

   struct RedCircle = TCircle + TRed;


   public static void t136()
   {
      RedCircle rc = new Object() 
      {
         double radius() { return 3.0; }
      };
      
      print(Math.round(rc.radius()));
      print(Math.round(rc.diameter()));
      print(rc.color() == Color.RED);
   }



   struct MCircle 
   {
      abstract double radius();
      double diameter() { return 2 * radius(); }
      String name() { return "Shape=Circle"; }
   }

   struct MRed 
   {
      Color color() { return Color.RED; }
      String name() { return "Color=Red"; }
   }

   struct CircleRed = MCircle MRed;
   

   public static void t162()
   {
      CircleRed cr = new Object()
      {
         double radius() { return 3.0; }
      };
      
      print(Math.round(cr.radius())); // ==  3);
      print(Math.round(cr.diameter())); //; == 6);
      print(Color.RED == cr.color());  // true
      print(cr.name()); // "Color=Red",  
   }



   // 
   struct U { constructor(); void m(); }

   static<T extends U> void f(T t) {
      U u = t.constructor();  // The returned value should be of type T ?
      u.m();
   }



   struct Subbable 
   {
      String substring(int i);
   }

   public static void t180()
   {
      Subbable sub = "whiteoak";
      print(sub.substring(5));
   }
   
   public static void main(String[] args) throws Exception
   {
      t1();
      t2();
      t74();
      t121();
      t136();
      t162();
      t180();
   }
}





