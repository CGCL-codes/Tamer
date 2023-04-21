/*
 * Copyright (C) 2007 Deutsche Telekom AG Laboratories
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.telekom.laboratories.multitouch.util;

import static java.util.Arrays.deepEquals;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Michael Nischt
 * @version 0.1
 */
public class LabelsTest {
    
    // <editor-fold defaultstate="collapsed" desc=" Example ">
    
    private interface Example
    {
        int[][] image();
        int[][] bounds();
    }
    
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc=" Attributes ">
    
    private final Example[] examples =
    {
        new Example()
        {
            private final int[][] image =
            {
                {  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 },
                {  0,  1,  0,  1,  0,  2,  2,  2,  0,  3,  3,  3,  0 },
                {  0,  1,  0,  1,  0,  2,  0,  0,  0,  3,  0,  3,  0 },
                {  0,  1,  0,  1,  0,  2,  2,  2,  0,  3,  3,  3,  0 },
                {  0,  1,  0,  1,  0,  0,  0,  2,  0,  3,  0,  3,  0 },
                {  0,  1,  1,  1,  0,  2,  2,  2,  0,  3,  0,  3,  0 },
                {  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 },  
            };

            private final int[][] bounds =
            {
                {  1,  1,  3,  5 },
                {  5,  1,  7,  5 },
                {  9,  1, 11,  5 },
            };              
            
            public int[][] image()
            {
                return image;
            }
            
            public int[][] bounds()
            {
                return bounds;
            }            
        },
        new Example()
        {
            private final int[][] image =
            {
                {  0,  0,  0,  0,  0,  0,  0 },
                {  0,  1,  1,  1,  1,  1,  0 },
                {  0,  1,  0,  0,  0,  1,  0 },
                {  0,  1,  0,  2,  0,  1,  0 },
                {  0,  1,  0,  0,  0,  1,  0 },
                {  0,  1,  1,  1,  1,  1,  0 },
                {  0,  0,  0,  0,  0,  0,  0 },  
            };

            private final int[][] bounds =
            {
                {  1,  1,  5,  5 },
                {  3,  3,  3,  3 },
            };               
            
            public int[][] image()
            {
                return image;
            }
            
            public int[][] bounds()
            {
                return bounds;
            }        
        },        
    };     
    
    // </editor-fold> 
        
    // <editor-fold defaultstate="collapsed" desc=" Initializers ">
    
    public LabelsTest() {
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Methods ">
    
    @Test
    public void testCount() 
    {
        for(final Example example : examples)
            testCount( example.image(), example.bounds().length );
    }
    
    private void testCount(int[][] image, int count) {

        final int[][] copy = image.clone();
        for(int i=0; i<copy.length; i++) {
            copy[i] = image[i].clone();
        }
        
        final Labels labels = new Labels(copy);
        final int c = labels.count();
        assertEquals("Number of labels is wrong", c, count);
        assertTrue
        (
            "Update doesn't behave as identity for a neutral image", 
            deepEquals(image, copy)
        );
    }
    
    @Test
    public void testBounds() {
        for(final Example example : examples)
            testBounds( example.image(), example.bounds() );
    }
    
    private void testBounds(int[][] image, int[][] bounds) {

        final int[][] copy = image.clone();
        for(int i=0; i<copy.length; i++) {
            copy[i] = image[i].clone();
        }
        
        Labels labels;
        
        labels = new Labels(copy);
        final int[][] b = labels.bounds();
        
        assertTrue("Bounds do not match", deepEquals(bounds, b));
        
        assertTrue
        (
            "Update doesn't behave as identity for a neutral image", 
            deepEquals(image, copy)
        );
    }    
    
    private static void print(int[][] image) {
        for(int[] line : image) {
            for(int pixel : line) {
                System.out.printf("%4d ", pixel);
            }
            System.out.println();
        }
        System.out.println();
    }   
    
    public static junit.framework.Test suite() { 
        return new JUnit4TestAdapter(LabelsTest.class); 
    }    
    
    // </editor-fold>
}import org.junit.Test;
import static org.junit.Assert.*;
import static java.util.Arrays.deepEquals;

public class LabelsTest
{
    interface Example
    {
        int[][] image();

        int[][] bounds();
    }
    
    Example[] examples =
    {
        // alternating
        new Example()
        {
            private final int[][] image =
            {
                {
                    1, 0, 2, 0, 3
                },
                {
                    0, 4, 0, 5, 0
                },
                {
                    6, 0, 7, 0, 8
                },
                {
                    0, 9, 0, 10, 0
                },
                {
                    11, 0, 12, 0, 13
                },
            };
            private final int[][] bounds =
            {
                //
                {
                    0, 0, 0, 0
                },
                {
                    2, 0, 2, 0
                },
                {
                    4, 0, 4, 0
                },
                //
                {
                    1, 1, 1, 1
                },
                {
                    3, 1, 3, 1
                },
                //
                {
                    0, 2, 0, 2
                },
                {
                    2, 2, 2, 2
                },
                {
                    4, 2, 4, 2
                },
                //
                {
                    1, 3, 1, 3
                },
                {
                    3, 3, 3, 3
                },
                //
                {
                    0, 4, 0, 4
                },
                {
                    2, 4, 2, 4
                },
                {
                    4, 4, 4, 4
                },
            };

            public int[][] image()
            {
                return image;
            }

            public int[][] bounds()
            {
                return bounds;
            }
        },
        // U S A
        new Example()
        {
            private final int[][] image =
            {
                {
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                },
                {
                    0, 1, 0, 1, 0, 2, 2, 2, 0, 3, 3, 3, 0
                },
                {
                    0, 1, 0, 1, 0, 2, 0, 0, 0, 3, 0, 3, 0
                },
                {
                    0, 1, 0, 1, 0, 2, 2, 2, 0, 3, 3, 3, 0
                },
                {
                    0, 1, 0, 1, 0, 0, 0, 2, 0, 3, 0, 3, 0
                },
                {
                    0, 1, 1, 1, 0, 2, 2, 2, 0, 3, 0, 3, 0
                },
                {
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                },
            };
            private final int[][] bounds =
            {
                {
                    1, 1, 3, 5
                },
                {
                    5, 1, 7, 5
                },
                {
                    9, 1, 11, 5
                },
            };

            public int[][] image()
            {
                return image;
            }

            public int[][] bounds()
            {
                return bounds;
            }
        },
        // inner dot in 0
        new Example()
        {
            private final int[][] image =
            {
                {
                    0, 0, 0, 0, 0, 0, 0
                },
                {
                    0, 1, 1, 1, 1, 1, 0
                },
                {
                    0, 1, 0, 0, 0, 1, 0
                },
                {
                    0, 1, 0, 2, 0, 1, 0
                },
                {
                    0, 1, 0, 0, 0, 1, 0
                },
                {
                    0, 1, 1, 1, 1, 1, 0
                },
                {
                    0, 0, 0, 0, 0, 0, 0
                },
            };
            private final int[][] bounds =
            {
                {
                    1, 1, 5, 5
                },
                {
                    3, 3, 3, 3
                },
            };

            public int[][] image()
            {
                return image;
            }

            public int[][] bounds()
            {
                return bounds;
            }
        },
        // failed once #1
        new Example()
        {
            private final int[][] image =
            {
                {
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                },
                {
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0
                },
                {
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0
                },
                {
                    0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0
                },
                {
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                },
            };
            private final int[][] bounds =
            {
                {
                    2, 1, 16, 3,
                },
            };

            public int[][] image()
            {
                return image;
            }

            public int[][] bounds()
            {
                return bounds;
            }
        },
        // failed once #2
        new Example()
        {
            private final int[][] image =
            {
                {
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                },
                {
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0
                },
                {
                    0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0
                },
                {
                    0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0
                },
                {
                    0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0
                },
                {
                    0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0
                },
                {
                    0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0
                },
                {
                    0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0
                },
                {
                    0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0
                },
                {
                    0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0
                },
                {
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                },
            };
            private final int[][] bounds =
            {
                {
                    2, 1, 12, 9,
                },
            };

            public int[][] image()
            {
                return image;
            }

            public int[][] bounds()
            {
                return bounds;
            }
        }
    };

    @Test
    public void testCount()
    {
        for (final Example example : examples)
            testCount(example.image(), example.bounds().length);
    }

    void testCount(int[][] image, int count)
    {

        final int[][] copy = image.clone();
        for (int i = 0; i < copy.length; i++)
        {
            copy[i] = image[i].clone();
        }

        final Labels labels = new Labels(copy);
        final int c = labels.count();
        assertEquals("Number of labels is wrong", c, count);
        assertTrue(
            "Update doesn't behave as identity for a neutral image",
            deepEquals(image, copy));
    }

    @Test
    public void testBounds()
    {
        for (final Example example : examples)
            testBounds(example.image(), example.bounds());
    }

    void testBounds(int[][] image, int[][] bounds)
    {

        final int[][] copy = image.clone();
        for (int i = 0; i < copy.length; i++)
        {
            copy[i] = image[i].clone();
        }

        Labels labels;

        labels = new Labels(copy);
        final int[][] b = labels.bounds();

        assertTrue("Bounds do not match", deepEquals(bounds, b));

        assertTrue(
            "Update doesn't behave as identity for a neutral image",
            deepEquals(image, copy));
    }

    static void print(int[][] image)
    {
        for (int[] line : image)
        {
            for (int pixel : line)
            {
                System.out.printf("%4d ", pixel);
            }
            System.out.println();
        }
        System.out.println();
    }
}
