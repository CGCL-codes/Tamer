package sprout.util;

/**
COPYRIGHT (C) 2000 Toby Donaldson. All Rights Reserved.

The intAccumulator interface.

@author Toby Donaldson
@version 1.0, May 31, 2000

*/
public interface intAccumulator {

    /**
     
      @return the accumulated value

    */
    public int value();

    /**
     
	 Look at an int value, and put it into this
	 accumulator.
	 
   	 @param n the next int to check

   */
    public void lookAt(int n);
}
