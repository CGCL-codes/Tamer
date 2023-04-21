package samples;

/*OUTPUT: "n=5"  */

public class SomethingSimple
{
   public static class Jac
   {
		public void f(int n)
      {
         System.out.println("n=" + n);
      }
   }
   
   
	public struct X
	{
		public void f(int n);
	}

	public void g(X x)
	{
		x.f(5);
	}
   
   public static void main(String[] args)
   {
      SomethingSimple a = new SomethingSimple();
      Jac jac = new Jac();
      X x = jac;
      a.g(x);
   }
}
