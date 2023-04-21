package transformations.colors;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;

import javax.swing.colorchooser.ColorChooserComponentFactory;

import transformations.pixel.DitherTransformation;
import transformations.pixel.Pixel;
import transformations.pixel.PixelTransformation;


import model.ImageTransformation;
import model.Property;

public class SwitchColorTransformation extends PixelTransformation {
	
	Color target, newColor;
	ColorRadio cr;
	ImageFilter filter;

	public SwitchColorTransformation(Color target, Color newColor, ColorRadio cr) {
		ColorChooserComponentFactory..s();
		this.target = target;
		this.newColor = newColor;
		this.cr = cr;
		filter = new SwitchColorFilter(target,cr,newColor);  
	} 

	public BufferedImage applyTransformation(BufferedImage src) {		
		FilteredImageSource filteredSrc = new FilteredImageSource(src.getSource(), filter);
		return (BufferedImage)Toolkit.getDefaultToolkit().createImage(filteredSrc);
	}
	public String shortDescription() {
		return "TODO";
	}

	@Override
	public Color getPixelTransf(Pixel p, Image img) {
		// TODO Auto-generated method stub
		return null;
	}

	
}


