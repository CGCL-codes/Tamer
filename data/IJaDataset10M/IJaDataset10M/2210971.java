package samples.recursive;

/* OUTPUT: "17,13,11,7,5,3,1", "57" */
public class RecursiveType
{
   public struct IntList 
   {
      public int getHead();
      public IntList getTail();
   }
   
   public static class IntListImpl
   {
      private final int x;
      private final IntListImpl xs;
         
      public IntListImpl(int y, IntListImpl ys)
      {
         x = y;
         xs = ys;
      }
      
      public int getHead() 
      {
         return x;
      }
      
      public IntListImpl getTail()
      {
         return xs;
      }
      
      public String toString()
      {
         if(xs == null)
            return "" + x;
         
         return x + "," + xs;
      }
   }
   
   
   public static int sum(IntList ns)
   {
      if(ns == null)
         return 0;
      return ns.getHead() + sum(ns.getTail());
   }
   
   public static IntListImpl make(int first, int... ns)
   {
      IntListImpl result = new IntListImpl(first, null);
      for(int i = 0; i < ns.length; ++i)
         result = new IntListImpl(ns[i], result);
      return result;
   }
   
   
   public static void main(String[] args)
   {
      IntList lst = make(1, 3, 5, 7, 11, 13, 17);
      System.out.println(lst);   
      System.out.println(sum(lst));
   }
}