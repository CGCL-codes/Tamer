package sanity;

public class ClassWithConsturcotMethod 
{
   public void f();
   
   public constructor();/*WHITEOAK COMPILATION ERROR EXPECTED: invalid method declaration; return type required*/
}