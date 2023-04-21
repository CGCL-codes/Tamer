// Copyright (c) 2004, William R. Burns
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification, are permitted 
// provided that the following conditions are met:
//
//  * Redistributions of source code must retain the above copyright notice, this list of 
//      conditions and the following disclaimer.
//  * Redistributions in binary form must reproduce the above copyright notice, this list of 
//      conditions and the following disclaimer in the documentation and/or other materials provided 
//      with the distribution.
//  * Neither the name of the Joctree Project nor the names of its contributors may be used to endorse 
//      or promote products derived from this software without specific prior written permission.
// 
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
// IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
// CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
// IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
// OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package net.wrburns.joctree;

import java.util.Vector;
import java.util.Iterator;
import net.wrburns.joctree.Vector3;
import net.wrburns.joctree.Plane;
import net.wrburns.joctree.Sphere;


public interface OctreeObject
{
  abstract Sphere getSphereBounds();
}


public class OctreeNode { 
  static final int X = 1, Y = 2, Z = 4;
  
  static int MAX_OBJECTS = 5; // may be adjusted to tune performance

  protected OctreeNode[8] children;
  protected boolean       isLeaf;
  protected Vector3       center;
  protected Vector        nonsubdividables, subdividables;
  protected double        width;

  public OctreeNode(Vector3 center, double width) {
    this.children = new OctreeNode[8];
    this.subdividables = new Vector();
    this.nonsubdividables = new Vector();
    this.center = center;
    this.width = width;
  }

  public void submit(OctreeObject obj) {
    int sect;
    
    sect = classify(obj);
    
    if(sect == -1)
      nonsubdividables.add(obj);
    else if(subdividables.size() <= MAX_OBJECTS)
      subdividables.add(obj);
    else {
      if(children[sect] == null)
        createSection(sect);
    
      children[sect].submit(obj);
      
      if(items.size() > MAX_OBJECTS)
        reclassify();
    }
  }
  
  protected void createSection(int sect) {
    Vector3 c = new Vector3();
    int newWidth = width/2;
    
    if(sect & X)
      c.x = center.x + newWidth;
    else
      c.x = center.x - newWidth;
      
    if(sect & Y)
      c.y = center.y + newWidth;
    else
      c.y = center.y - newWidth;
    
    if(sect & Z)
      c.z = center.z + newWidth;
    else
      c.z = center.z - newWidth;
  
    children[sect] = new OctreeNode(c, width/2);
  }
  
  public void reclassify() {  
    OctreeObject  obj;
    int           sect;
        
    for(Iterator i = subdividables.iterator(); i.hasNext();) {
      obj = i.next();
      sect = classify(obj);
      
      if(children[sect] == null)
        createSection(sect);
      
      children[sect].submit(obj);
    }
  }
  
  public boolean isLeaf() {
    return this.isLeaf;
  }
  
  protected int classify(OctreeObject obj) {
    int section = 0;
    
    Plane xp, yp, zp;
    Sphere bounds = obj.getBounds();
    
    xp = new Plane(0, 0, 1, center.x);
    yp = new Plane(1, 0, 0, center.y);
    zp = new Plane(0, 1, 0, center.z);
    
    if(xp.intersects(bounds) || yp.intersects(bounds) || zp.intersects(bounds))
      return -1;
    
    section += ((xp.distance(bounds.center) >= 0) * X);
    section += ((yp.distance(bounds.center) >= 0) * Y);
    section += ((zp.distance(bounds.center) >= 0) * Z);
    
    return section;
  }
}

