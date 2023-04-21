package samples.generics;

import java.awt.Color;
import whiteoak.lang.*;

/*OUTPUT: "java.lang.Thread" */
public class StructsAsConstraintsInClass
{
   public static class Cell<T extends Nameable>
   {
      private final T value;
      public Cell(T t) { value = t; }
      
      public String toString() { return value.getName(); }
   }
   
   public struct Nameable
   {
      public String getName();
   }
   
      
   public static void main(String[] args)
   {
      Cell c = new Cell<Class>(Thread.class);
      System.out.println(c);
   }
}