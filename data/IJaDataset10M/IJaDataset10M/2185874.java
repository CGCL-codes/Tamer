
package samples;

/*OUTPUT: "abc", "xyz", "xyz" */
public class AssignmentFromInstantiatedGenericWithField
{
   public struct Jas
   {
      public int n;
      public String value;
   }
   
   public static class Jac<T>
   {
      public int n;
      public T value;
   }
      
   public static void main(String[] args)
   {
      Jac<String> a = new Jac<String>();
      a.value = "abc";
      
      Jas b = a;
      System.out.println(b.value);
      
      b.value = "xyz";
      System.out.println(b.value);
      System.out.println(a.value);      
   }
}
