package byggeTegning.husGeometri;

import javax.media.j3d.Appearance;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import byggeTegning.geometry.Object3D;
import byggeTegning.geometry.PolyBody;
import byggeTegning.geometry.TransformedGroup3D;


// ********************************************************************
// *** CLASS: Plank
// ********************************************************************
public class Plank extends TransformedGroup3D
{
  double plankWidth;
  double plankHeight;
//  double plankLength;
  
  //  public Point3D p0={x,y,z}; // Lower back left corner in 3D coordinate space

  private static final float width=1.0f;
  private static final float height=1.0f;

  // Point3f p=new Point3f(0.0f,0.0f,0.0f);
  private static final float px=0.0f;
  private static final float py=0.0f;
  private static final float pz=0.0f;

  // Point3f q=new Point3f(0.0f,0.0f,1.0f);
  private static final float qx=0.0f;
  private static final float qy=0.0f;
  private static final float qz=1.0f;

  // Front face closed polygon. Forside: punktene nevnes 'mot klokka'
  private static final Point3f[] defaultFront
      = { new Point3f(qx,qy,qz),
          new Point3f(qx+width,qy,qz),
          new Point3f(qx+width,qy+height,qz),
          new Point3f(qx,qy+height,qz),
          new Point3f(qx,qy,qz)
        };    

  // Back face closed polygon. Forside: punktene nevnes 'mot klokka'
  private static final Point3f[] defaultBack
      = { new Point3f(px,py,pz),
          new Point3f(px,py+height,pz),
          new Point3f(px+width,py+height,pz),
          new Point3f(px+width,py,pz),
          new Point3f(px,py,pz),
        };    
  
  
  // ******************************************************************
  // *** CONTRUCTOR: Plank -- NY UTGAVE
  // ******************************************************************
  public Plank(double plankWidth,double plankHeight)
  { this.plankWidth=plankWidth;
    this.plankHeight=plankHeight;
  }
  
    
  
  // ******************************************************************
  // *** METHOD: getModel3D 
  // ******************************************************************
  public TransformedGroup3D getModel3D
  ( Color3f c,
    double x,
    double y,
    double z,
    double length,
    boolean directionNorthSouth
//    ,
//    float topCut,
//    float botCut
  )
  {
    Point3f[] front=Plank.defaultFront;
//    Point3f[] front=this.skråFront();
    Point3f[] back=Plank.defaultBack;
//    Point3f[] back=this.skråBack();
    
    return(makeGroup(c,x,y,z,length,directionNorthSouth,front,back));
  }
  
  // ******************************************************************
  // *** METHOD: getModel3DSkrå 
  // ******************************************************************
  public TransformedGroup3D getModel3DSkrå
  ( Color3f c,
    double x,
    double y,
    double z,
    double length,
    double tanTakVinkel,  
    boolean directionNorthSouth,
    boolean skråFront,
    boolean skråBack  
  )
  {
    Point3f[] front=Plank.defaultFront;
    Point3f[] back=Plank.defaultBack;
    if(skråFront) front=this.skråFront(tanTakVinkel,length);
    if(skråBack)  back=this.skråBack(tanTakVinkel,length);

    return(makeGroup(c,x,y,z,length,directionNorthSouth,front,back));
  }
  
  // ******************************************************************
  // *** METHOD: getModel3DSkråFront 
  // ******************************************************************
  public TransformedGroup3D getModel3DSkråFront
  ( Color3f c,
    double x,
    double y,
    double z,
    double length,
    double tanTakVinkel,  
    boolean directionNorthSouth
  )
  {
//    Point3f[] front=this.defaultFront;
    Point3f[] front=this.skråFront(tanTakVinkel,length);
    Point3f[] back=Plank.defaultBack;
//    Point3f[] back=this.skråBack(tanTakVinkel,length);

    return(makeGroup(c,x,y,z,length,directionNorthSouth,front,back));
  }
  
  // ******************************************************************
  // *** METHOD: getModel3DSkråBack 
  // ******************************************************************
  public TransformedGroup3D getModel3DSkråBack
  ( Color3f c,
    double x,
    double y,
    double z,
    double length,
    double tanTakVinkel,  
    boolean directionNorthSouth
  )
  {
    Point3f[] front=Plank.defaultFront;
//    Point3f[] front=this.skråFront(tanTakVinkel,length);
//    Point3f[] back=this.defaultBack;
    Point3f[] back=this.skråBack(tanTakVinkel,length);

    return(makeGroup(c,x,y,z,length,directionNorthSouth,front,back));
  }
    
  
  // ******************************************************************
  // *** METHOD: makeGroup 
  // ******************************************************************
  private TransformedGroup3D makeGroup
  ( Color3f c,
    double x,
    double y,
    double z,
    double length,
    boolean directionNorthSouth,
    Point3f[] front,
    Point3f[] back
  )
  { TransformedGroup3D tg=new TransformedGroup3D();
    tg.setSize(plankWidth,plankHeight,length);
    tg.setP0(x,y,z);
    if(!directionNorthSouth) tg.setAngleY(Math.PI/2); // Bare hvis Øst-Vest vegg  
    Appearance app=Object3D.createAppearance(8,c);
    
    Shape3D body=new PolyBody(back,front);
    body.setAppearance(app);
    tg.addChild(body);
    
    Shape3D frontEnd=new byggeTegning.geometry.Polygon(front);
    frontEnd.setAppearance(app);
    tg.addChild(frontEnd);
    
    Shape3D backEnd=new byggeTegning.geometry.Polygon(back);
    backEnd.setAppearance(app);
    tg.addChild(backEnd);

    return(tg);
  }
    
  // ******************************************************************
  // *** PRIVATE: skråFront 
  // ******************************************************************
  private Point3f[] skråFront(double tanTakVinkel,double length)
  { float fac=(float)(plankHeight/(length*tanTakVinkel));
    float dz=Math.min(height*fac,0.9999f);
    Point3f[] front
      = { new Point3f(qx,qy,qz),
          new Point3f(qx+width,qy,qz),
          new Point3f(qx+width,qy+height,qz-dz),
          new Point3f(qx,qy+height,qz-dz),
          new Point3f(qx,qy,qz)
        };    
    return(front);
  }
    
  // ******************************************************************
  // *** PRIVATE: skråBack 
  // ******************************************************************
  private Point3f[] skråBack(double tanTakVinkel,double length)
  { float fac=(float)(plankHeight/(length*tanTakVinkel));
    float dz=Math.min(height*fac,0.9999f);
    Point3f[] back
      = { new Point3f(px,py,pz),
          new Point3f(px,py+height,pz+dz),
          new Point3f(px+width,py+height,pz+dz),
          new Point3f(px+width,py,pz),
          new Point3f(px,py,pz)
        };    
    return(back);
  }

}