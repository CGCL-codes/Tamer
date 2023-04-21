package dominio;

public class Alojamiento{
     
     public enum Pension{MEDIA_PENSION, PENSION_COMPLETA};
     public enum Tipo{CABAÑA, HOTEL, CASA, CRUCERO, CAMPING};
     private String nombre;
     private Tipo tipo;
     private int estrellas;
     private Pension pension;
     
     public String getNombre(){
          return this.nombre;
     }
     
     public Tipo getTipo(){
          return this.tipo;
     }
     
     public int getEstrellas(){
          return this.estrellas;
     }
     
     public Pension getPension(){
          return this.pension;
     }
     
     public void setNombre(String nombreP){
          this.nombre = nombreP;
     }
     
     public void setTipo(Tipo tipoP){
          boolean encontre = false;
          if(!encontre){
               for(Tipo elemento: Tipo.values()){
                    if(elemento == tipoP){
                         this.tipo = tipoP; 
                         encontre = true;
                    }
               }
          }
          else if(!encontre){
               this.tipo = Tipo.HOTEL;
          }
     }
     
     public void setEstrellas(int estrellasP){
          if(0 < estrellasP && estrellasP < 7){
               this.estrellas = estrellasP;
          }else{
               this.estrellas = 3;
          }
     }
     
     public void setPension(Pension pensionP){
          if(pensionP == Pension.MEDIA_PENSION || pensionP == Pension.PENSION_COMPLETA){
               this.pension = pensionP;
          }else{
               this.pension = Pension.PENSION_COMPLETA;
          }
     }
     
     public Alojamiento(){
          
          this.setNombre("sinNombre");
          this.setTipo(Tipo.HOTEL);
          this.setEstrellas(0);
          this.setPension(Pension.MEDIA_PENSION);
          
     }
     
     public Alojamiento(String nombreP, Tipo tipoP, int estrellasP, Pension pensionP){
          
          this.setNombre(nombreP);
          this.setTipo(tipoP);
          this.setEstrellas(estrellasP);
          this.setPension(pensionP);
          
     }
     
     public Alojamiento(Alojamiento a){
          
          this.setNombre(a.getNombre());
          this.setTipo(a.getTipo());
          this.setEstrellas(a.getEstrellas());
          this.setPension(a.getPension());
     }
     
     public String toString(){
          
          return "Nombre: " + this.nombre + this.tipo + this.estrellas + " Estrellas " + this.pension;
          
     }
     
     public boolean equals(Object o){
          
          boolean esIgual = false;
          if(this.nombre.equals(((Alojamiento)o).getNombre()) && this.tipo.equals(((Alojamiento)o).getTipo())){
               esIgual = true;
          }
          
          return esIgual;
          
     }
     
}