package samples.sisters;

import java.util.*;

/*OUTPUT: "10"  */
public class Sister
{
   public struct S 
   {
      public int f(int x) { return x; }
      public int g() { return 10; } 
   }

   public static class C
   {
      public int f(int x)
      {
         return x * 2;
      }
   }
   
   public static void main(String[] args)
   {
      S s = new C();
      System.out.println(s.g());
   }
}
