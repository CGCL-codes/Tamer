// $Id: BigLayout.java,v 1.1.1.1 2005/01/15 20:12:12 fredgc Exp $

package net.sourceforge.jmisc;

import java.awt.*;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

/** BigLayout is a special layout.
    It puts a large canvas in the middle, some scroll bars along the
    bottom, and some buttons on the right.
    
    
    @author <a href=mailto:"fredgc@users.sourceforge.net">Fred Gylys-Colwell</a>
    @version $Name:  $,  $Revision: 1.1.1.1 $

  */

public class BigLayout implements LayoutManager {
  
  private int vgap, hgap;
  Component big;
  Vector buttons, scrolls;
  int nc=2;

  public final static String BUTTON = "Put In Button Bar.";
  public final static String BIG = "Large Canvas.";
  public final static String SCROLL = "Scroll Bar below Large Canvas.";
  
  public BigLayout() {
    this(2,2);
  }
  
  public BigLayout(int v, int h) {
    vgap = v;
    hgap = h;
    big = null;
    buttons = new Vector();
    scrolls = new Vector();
  }

  public int getColumns() {
    return nc;
  }
  public void setColumns(int n) {
    nc = n;
  }
  
  
  public void addLayoutComponent(String name, Component comp) {
    if( name.equals(BIG) ) {
      big = comp;
    } else if( name.equals(BUTTON)) {
      buttons.addElement(comp);
    } else if( name.equals(SCROLL)) {
      scrolls.addElement(comp);
    } else {
      throw new IllegalArgumentException("Wrong panel region for BigLayout:"
					 + name);
    }
  }
  
  public void removeLayoutComponent(Component comp) {
    if( comp == big ) big = null;
    buttons.removeElement(comp);
    scrolls.removeElement(comp);
  }
  
  public Dimension minimumLayoutSize(Container parent) {
    return layoutSize(parent, false);
  }
  public Dimension preferredLayoutSize(Container parent) {
    return layoutSize(parent, true);
  }

  Dimension componentSize(Component c, boolean prefer) {
    if( prefer ) {
      return c.getPreferredSize();
    } else {
      return c.getMinimumSize();
    }
  }
  
  /* Required by LayoutManager. */
  Dimension layoutSize(Container parent, boolean prefer) {

    // Find height and width of buttons.
    Dimension b = buttonSize(prefer);
    if( big != null) {
      Dimension d = componentSize(big, prefer);
      b.width += hgap + d.width;
      b.height = Math.max(b.height, d.height + vgap);
    } 
    Dimension s = scrollSize(prefer);

    Dimension dim = new Dimension(0, 0);
    dim.width = Math.max( s.width, b.width);
    dim.height =  b.height + s.height;

    //Always add the container's insets!
    Insets insets = parent.getInsets();
    dim.width += insets.left + insets.right;
    dim.height += insets.top + insets.bottom;

    return dim;
  }

  Dimension buttonSize(boolean prefer) {
    int bw = 0;
    int bh = 0;
    for(int i=0; i< buttons.size(); i++) {
      Component b = (Component) buttons.elementAt(i);
      if( b.isVisible() ) {
	Dimension d = componentSize(b,prefer);
	bw = Math.max(bw, d.width);
	bh = bh + d.height + vgap;
      }
    }
    return new Dimension(bw,bh);
  }
  int countScrolls() {
    int n=0;
    for(int i=0; i < scrolls.size() ; i++) {
      Component s = (Component) scrolls.elementAt(i);
      if( s.isVisible() ) n++;
    }
    return n;
  }

  Dimension scrollSize(boolean prefer) {
    int height = 0;
    int width = 0;
    int ns = countScrolls();
    int nl =  (ns + nc-1)/nc; // Number of scrolls per column.
				// (Add nc-1 to numerator so that java
				// rounds up. 

    Enumeration enum = scrolls.elements();
    for( int col = 0; col < nc; col++) {
      int w = 0;
      int h = 0;
      int i = 0;
      while( (i < nl) &&(enum.hasMoreElements()) ) {
	Component s = (Component) enum.nextElement();
	if( s.isVisible() ) {
	  Dimension d = componentSize(s,prefer);
	  w = Math.max(w, d.width);
	  h += d.height + vgap;
	  i++;
	}
      }
      h -= vgap;		// Subtract the gap at the bottom.
      width += w + hgap;
      height = Math.max(height, h);
    }
    width -= hgap;		// Subtract the gap at the right.
    return new Dimension(width, height);
  }
  
  public void layoutContainer(Container parent) {
    Insets insets = parent.getInsets();
    Dimension dim = parent.getSize();
    boolean prefer = true;
    Dimension b = buttonSize(prefer);
    Dimension s = scrollSize(prefer);

    while( (nc < 5) && (2*s.height > dim.height ) ) {
      nc++;
      b = buttonSize(prefer);
      s = scrollSize(prefer);
    }
    while( (nc> 1 ) && (10*s.height < dim.height) ) {
      nc--;
      b = buttonSize(prefer);
      s = scrollSize(prefer);
    }
    
    if( big != null ) {
      big.setBounds( insets.left, insets.top,
		   dim.width - b.width - hgap - insets.right - insets.left,
		   dim.height - s.height - vgap - insets.top - insets.bottom);
    }

    // Line the buttons up on the right.
    int x = dim.width - insets.right - b.width;
    int y = insets.top;
    for(int i =0; i< buttons.size(); i++) {
      Component c = (Component)buttons.elementAt(i);
      if( c.isVisible() ) {
	Dimension d = componentSize(c,prefer);
	c.setBounds(x,y, b.width, d.height);
	y += d.height + vgap;
      }
    }

    // Put the scrolls in columns.
    int w = (dim.width - insets.left - insets.right) / nc;
    int ns = countScrolls();
    int nl =  (ns + nc-1)/nc; // Number of scrolls per column.
    x = insets.left;

    Enumeration enum = scrolls.elements();
    for( int col=0; col < nc; col++) {
      y = dim.height - insets.bottom - s.height;
      int i=0;
      while( (i < nl) && ( enum.hasMoreElements()) ) {
	Component c = (Component) enum.nextElement();
	if( c.isVisible() ) {
	  Dimension d = componentSize(c,prefer);
	  c.setBounds(x,y, w, d.height);
	  y += d.height + vgap;
	  i++;
	}
      }
      x += w + hgap;
    }
  }
  
  public String toString() {
    return getClass().getName() + "[vgap=" + vgap + ", hgap="+hgap+ "]";
  }
}


