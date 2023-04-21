package samples.composition;

/*OUTPUT:  "1", "1", "2", "2", "1"*/
public class MixinOperator
{
   public struct Text 
   {
      public final String text;
      public void setText(String s);
   }

   
   public struct Swap 
   {
      public String swap(String s);
   }
   
   
   public struct SwapText = Swap Text;

   
   public static class TextImpl
   {
      public String text;
      
      public void setText(String s) 
      {
         text = s;
      }
      
      public String swap(String s)
      {
         String result = text;
         text = s;
         return result;
      }      
   }
   

   public static void main(String[] args)
   {      
      SwapText st = new TextImpl();
      st.setText("1");
      System.out.println(st.text);
      
      System.out.println(st.swap("2"));
      System.out.println(st.text);

      System.out.println(st.swap("1"));
      System.out.println(st.text);
   }
}
