package samples.traits;

/*OUTPUT: "3"*/
public class TraitsWithOverriding
{      
   public struct A
   {
      public int one() { return 1; }
      public int three() { return -3; }
   }
   
   public struct B 
   {
      public int one() { return -1; }
      public int two() { return 2; }
      public int three() { return -333; }
   }
   
   public struct More
   {
      public abstract int one();
      public abstract int two();
         
      public int onePlusTwo()
      {
         return one() + two();
      }
   }
   
   public static class Impl
   {
      public int three() { return 3; }
   }
   
   public struct C = A + B + More;
      
   public static void main(String[] args)
   {
      C c = new Impl(); /*WHITEOAK COMPILATION ERROR EXPECTED: structural mismatch: required member one has no matching member in samples.traits.TraitsWithOverriding.Impl
found   : samples.traits.TraitsWithOverriding.Impl
required: samples.traits.TraitsWithOverriding.C*/
      
     System.out.println(c.onePlusTwo());
   }
}