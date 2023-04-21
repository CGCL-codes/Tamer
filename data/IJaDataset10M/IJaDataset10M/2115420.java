package samples.fields;


import samples.Person;

public class StructWithFieldFailure
{
   public struct Nameable
   {
      public String first_;
      public String last;
      public int year;
   }
      
   public static void read(Nameable n)
   {
      System.out.println(n.last + ", " + n.first_);
      System.out.println(n.year);
   }
   
   public static void write(Nameable n)
   {
      n.last = "Levinzon";
      n.year = 93;
   }
   
   public static void main(String[] args)
   {
      Person p = new Person(1993, "Barry", "Levinson", null);
      read(p);/*WHITEOAK COMPILATION ERROR EXPECTED: read(samples.fields.StructWithFieldFailure.Nameable) in samples.fields.StructWithFieldFailure cannot be applied to (samples.Person)*/
      write(p);/*WHITEOAK COMPILATION ERROR EXPECTED: write(samples.fields.StructWithFieldFailure.Nameable) in samples.fields.StructWithFieldFailure cannot be applied to (samples.Person)*/
      read(p);/*WHITEOAK COMPILATION ERROR EXPECTED: read(samples.fields.StructWithFieldFailure.Nameable) in samples.fields.StructWithFieldFailure cannot be applied to (samples.Person)*/
   }
}